package com.example.kalmarium.ui.screen.eladasok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.data.KategoriaEntity
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.KategoriaRepository
import com.example.kalmarium.data.repository.TermekRepository
import com.example.kalmarium.data.repository.VasarRepository
import com.example.kalmarium.utils.SnackbarManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.first


class EladasViewModel(
    private val vasarId: Int,
    private val termekRepository: TermekRepository,
    private val eladasRepository: EladasRepository,
    private val kategoriaRepository: KategoriaRepository,
    private val vasarRepository: VasarRepository

) : ViewModel() {




    val vasarNev: StateFlow<String> =
        vasarRepository.getAll()
            .map { list ->
                list.find { it.id == vasarId }?.nev ?: ""
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ""
            )

    // =====================================================
// ELADÁS LISTA
// =====================================================

    val eladasLista: StateFlow<List<EladasEntity>> =
        eladasRepository
            .getEladasokVasarhoz(vasarId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )


    // =====================================================
    // TERMÉK LISTA
    // =====================================================

    val termekLista: StateFlow<List<TermekEntity>> =
        termekRepository.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    // =====================================================
    // KATEGÓRIA LISTA (VALÓDI DAO-BÓL)
    // =====================================================

    val kategoriaLista: StateFlow<List<KategoriaEntity>> =
        kategoriaRepository.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )



    private val vasarFlow: Flow<VasarEntity?> =
        vasarRepository.getAll()
            .map { list -> list.find { it.id == vasarId } }







    private fun getProfitMessage(profit: Int): String =
        when {
            profit < 0 -> "A helypénz sincs meg… \uD83D\uDE2C"
            profit < 5_000 -> "Nem érte meg felkelni… \uD83D\uDE34"
            profit < 10_000 -> "Ezért se érte meg felkelni!"
            profit < 15_000 -> "Ez már kávépénz egész hónapra ☕"
            profit < 20_000 -> "Alakul ez! 📈"
            profit < 25_000 -> "Szépen csordogál a profit 💸"
            profit < 30_000 -> "Ez már egy jó hétvége ára 😎"
            profit < 35_000 -> "A kassza kezd mosolyogni 😏"
            profit < 40_000 -> "Ez már nem hobbi! 🔥"
            profit < 45_000 -> "Komolyan veszi magát a biznisz 💼"
            profit < 50_000 -> "Fél százas! 🎉"
            profit < 55_000 -> "Ez már vállveregetős szint 👏"
            profit < 60_000 -> "Stabil, mint a beton 🧱"
            profit < 65_000 -> "A könyvelő is elégedett 📊"
            profit < 70_000 -> "Ez már komoly pálya 🏎️"
            profit < 75_000 -> "A profit izmosodik 💪"
            profit < 80_000 -> "Itt már pezsgőt lehet bontani 🥂"
            profit < 85_000 -> "Ez már majdnem prémium kategória ✨"
            profit < 90_000 -> "Nagyon szép nap ez! 🌞"
            profit < 95_000 -> "Ez már bajnok szint 🏆"
            profit < 100_000 -> "Mindjárt százas! 🎯"
            else -> "Hat számjegy! 👑 Itt már legenda vagy!"
        }


    private var lastInsertedEladas: EladasEntity? = null
    private var undoJob: Job? = null

    private suspend fun resolveTimestampForSale(): Long {

        val vasar = vasarRepository.getAll()
            .first()
            .find { it.id == vasarId }



        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

        val todayString = formatter.format(Date())

        // Ha mai vásár → pontos idő
        if (vasar?.datum == todayString) {
            return System.currentTimeMillis()
        }

        // Régi vásár → adott nap 00:00
        val parsedDate = formatter.parse(vasar?.datum ?: todayString)
            ?: return System.currentTimeMillis()

        val cal = Calendar.getInstance()
        cal.time = parsedDate
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.timeInMillis
    }


    fun insertEladas(termek: TermekEntity, ar: Int) {
        viewModelScope.launch {




            // 🔥 1. KÉSZLET CSÖKKENTÉS (nem mehet mínuszba)
            val ujKeszlet = (termek.keszlet - 1).coerceAtLeast(0)

            termekRepository.update(
                termek.copy(keszlet = ujKeszlet)
            )

            // 🔥 2. ELADÁS LÉTREHOZÁSA
            val newEladas = EladasEntity(
                vasarId = vasarId,
                termekNev = termek.nev,
                kategoria = termek.kategoria,
                mennyiseg = 1,
                eladasiAr = ar,
                timestamp = resolveTimestampForSale()
            )

            eladasRepository.insert(newEladas)

            // 🔥 Bevétel frissítése az adott vásárnál
            val vasar = vasarRepository.getAll()
                .first()
                .find { it.id == vasarId }

            vasar?.let {
                val ujBevetel = it.bevetel + ar

                vasarRepository.updateVasar(
                    it.copy(bevetel = ujBevetel)
                )
            }








            vasar?.let {

                val ujBevetel = it.bevetel + ar

                vasarRepository.updateVasar(
                    it.copy(bevetel = ujBevetel)
                )

                val profit = ujBevetel - it.koltseg
                val profitText = getProfitMessage(profit)

                SnackbarManager.showMessage(
                    "Eladás rögzítve • ${ar} Ft\n" +
                            "${termek.nev} készlet: $ujKeszlet db\n" +
                            profitText
                )
            }
        }
    }


    fun undoLastEladas() {
        viewModelScope.launch {

            lastInsertedEladas?.let { eladas ->

                // 🔥 1. Eladás törlés
                eladasRepository.delete(eladas)

                // 🔥 2. Készlet visszanövelés
                val termek = termekLista.value
                    .find { it.nev == eladas.termekNev }

                termek?.let {
                    termekRepository.update(
                        it.copy(keszlet = it.keszlet + 1)
                    )
                }

                lastInsertedEladas = null
            }
        }
    }


    fun deleteEladas(eladas: EladasEntity) {
        viewModelScope.launch {
            eladasRepository.delete(eladas)
        }
    }

    // =====================================================
    // 🔥 KATEGÓRIA ÁTRENDEZÉS (VALÓDI KATEGÓRIA ENTITY)
    // =====================================================

    fun reorderKategoriak(from: Int, to: Int) {
        viewModelScope.launch {

            val current = kategoriaLista.value
                .sortedBy { it.sorrend }
                .toMutableList()

            if (from !in current.indices || to !in current.indices) return@launch

            val item = current.removeAt(from)
            current.add(to, item)

            termekRepository.updateKategoriakOrder(current)
        }
    }

    // =====================================================
    // 🔥 TERMÉK ÁTRENDEZÉS
    // =====================================================

    fun reorderTermekek(kategoriaId: Int, from: Int, to: Int) {
        viewModelScope.launch {

            val current = termekLista.value
                .filter { it.kategoriaId == kategoriaId }
                .sortedBy { it.sorrend }
                .toMutableList()

            if (from !in current.indices || to !in current.indices) return@launch

            val item = current.removeAt(from)
            current.add(to, item)

            termekRepository.updateTermekOrder(current)
        }
    }

    fun updateTermekOrderFull(
        kategoriaId: Int,
        items: List<TermekEntity>
    ) {
        viewModelScope.launch {
            termekRepository.updateTermekOrder(items)
        }
    }

    // ============================
// KATEGÓRIA UPDATE
// ============================

    fun updateKategoria(kategoria: KategoriaEntity) {
        viewModelScope.launch {
            kategoriaRepository.update(kategoria)
        }
    }

// ============================
// KATEGÓRIA DELETE
// ============================

    fun deleteKategoria(kategoria: KategoriaEntity) {
        viewModelScope.launch {
            kategoriaRepository.delete(kategoria)
        }
    }

    // =============================
// TERMÉK UPDATE
// =============================
    fun updateTermek(termek: TermekEntity) {
        viewModelScope.launch {
            termekRepository.update(termek) // használd az update-et, amit a repositoryban definiáltál
        }
    }

    // =============================
// TERMÉK DELETE
// =============================
    fun deleteTermek(termek: TermekEntity) {
        viewModelScope.launch {
            termekRepository.delete(termek)
        }
    }

    fun insertKategoria(kategoria: KategoriaEntity) {
        viewModelScope.launch {
            kategoriaRepository.insert(kategoria)
        }
    }

    fun insertTermek(termek: TermekEntity) {
        viewModelScope.launch {
            termekRepository.insert(termek)
        }
    }

    fun deleteAllForVasar() {
        viewModelScope.launch {
            eladasRepository.deleteAllForVasar(vasarId)
        }
    }

    fun insertTetelesEladas(termekek: List<TermekEntity>) {
        viewModelScope.launch {

            val osszeg = termekek.sumOf { it.ar }


            // 🔥 Csoportosítás darabszám szerint
            val grouped = termekek.groupingBy { it.id }.eachCount()

            // 🔥 Készlet csökkentés
            grouped.forEach { (termekId, darab) ->

                val termek = termekLista.value.find { it.id == termekId }
                    ?: return@forEach

                val ujKeszlet =
                    (termek.keszlet - darab).coerceAtLeast(0)

                termekRepository.update(
                    termek.copy(keszlet = ujKeszlet)
                )
            }

            // 🔥 Eladások mentése külön-külön
            termekek.forEach { termek ->

                val newEladas = EladasEntity(
                    vasarId = vasarId,
                    termekNev = termek.nev,
                    kategoria = termek.kategoria,
                    mennyiseg = 1,
                    eladasiAr = termek.ar,
                    timestamp = resolveTimestampForSale()

                )

                eladasRepository.insert(newEladas)

                val vasar = vasarRepository.getAll()
                    .first()
                    .find { it.id == vasarId }

                vasar?.let {
                    val ujBevetel = it.bevetel + osszeg

                    vasarRepository.updateVasar(
                        it.copy(bevetel = ujBevetel)
                    )
                }

            }



            val frissLista = termekLista.value

            val keszletInfo = grouped.mapNotNull { (termekId, _) ->
                val t = frissLista.find { it.id == termekId }
                t?.let { "${it.nev}: ${it.keszlet} db" }
            }.joinToString("\n")

            SnackbarManager.showMessage(
                "Készlet:\n$keszletInfo"
            )
        }
    }







}
