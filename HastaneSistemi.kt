import java.time.LocalDate

abstract class Kisi(
    val id: Int,
    val ad: String,
    val soyad: String,
    val yas: Int
) {
    abstract fun bilgileriGoster(): String
    abstract fun gorevTanimi(): String

    open fun tamAd(): String = "$ad $soyad"
    open fun selamVer(): String = "Merhaba, ben ${tamAd()}."

    override fun toString(): String = "[#$id] ${tamAd()} ($yas yaş)"
}
class Doktor(
    id: Int,
    ad: String,
    soyad: String,
    yas: Int,
    val uzmanlik: String,
    val poliklinik: String,
    val sicilNo: String,
    private var aktif: Boolean = true,            
    private var muayeneSayisi: Int = 0             
) : Kisi(id, ad, soyad, yas) {

    override fun tamAd(): String = "Dr. ${super.tamAd()}"

    override fun bilgileriGoster(): String =
        "👨‍⚕️ DOKTOR | Ad: ${tamAd()} | Sicil: $sicilNo | Uzmanlık: $uzmanlik | " +
        "Poliklinik: $poliklinik | Muayene: $muayeneSayisi | Durum: ${if (aktif) "Aktif" else "Pasif"}"

    override fun gorevTanimi(): String = "Poliklinik hekimi — $uzmanlik birimi"

    override fun selamVer(): String = "Merhaba, ben ${tamAd()}. $uzmanlik uzmanıyım."

    fun durumDegistir() { aktif = !aktif }
    fun isAktif(): Boolean = aktif

    fun muayeneYap() {
        if (!aktif) { println("⚠️  ${tamAd()} şu an aktif değil."); return }
        muayeneSayisi++
        println("✅ Muayene tamamlandı. Toplam: $muayeneSayisi")
    }

    fun muayeneSayisiniGetir(): Int = muayeneSayisi  
}

class Hasta(
    id: Int,
    ad: String,
    soyad: String,
    yas: Int,
    val tcNo: String,
    val kanGrubu: String,
    val kronikHastalik: String = "Yok",
    private val notlar: MutableList<String> = mutableListOf(),        
    private val randevuGecmisi: MutableList<String> = mutableListOf() 
) : Kisi(id, ad, soyad, yas) {

    val kayitTarihi: LocalDate = LocalDate.now()
    override fun bilgileriGoster(): String =
        "🧑 HASTA | Ad: ${tamAd()} | TC: $tcNo | Kan: $kanGrubu | " +
        "Kronik: $kronikHastalik | Kayıt: $kayitTarihi | Not: ${notlar.size} | Randevu: ${randevuGecmisi.size}"

    override fun gorevTanimi(): String = "Hasta — Kan grubu: $kanGrubu"

    fun notEkle(not: String) {
        notlar.add("[${LocalDate.now()}] $not")
        println("📝 Not eklendi: $not")
    }

    fun notlariGetir(): List<String> = notlar.toList()
    fun randevuEkle(bilgi: String) = randevuGecmisi.add(bilgi)
    fun randevuGecmisiniGetir(): List<String> = randevuGecmisi.toList()
}


class Randevu(
    val id: Int,
    val hasta: Hasta,
    val doktor: Doktor,
    val tarih: LocalDate,
    val sebep: String
) {
    fun tamamla() {
        doktor.muayeneYap()
        hasta.randevuEkle("$tarih — ${doktor.tamAd()} — $sebep")
        println("📅 Randevu #$id tamamlandı: ${hasta.tamAd()} → ${doktor.tamAd()}")
    }
}

fun main() {
    println("🏥  HASTANE YÖNETİM SİSTEMİ\n${"═".repeat(50)}")

    val doktor1 = Doktor(1, "Mehmet", "Yılmaz", 48, "Kardiyoloji", "Kalp Polikliniği",  "SCL-001")
    val doktor2 = Doktor(2, "Ayşe",   "Kaya",   39, "Nöroloji",    "Sinir Polikliniği", "SCL-002")
    val hasta1  = Hasta(101, "Zeynep", "Öztürk", 34, "11122233344", "A+", "Hipertansiyon")
    val hasta2  = Hasta(102, "Murat",  "Güneş",  52, "22233344455", "B-")

    println("\n── bilgileriGoster() — Polymorphism ─────────────────")
    val kisiler: List<Kisi> = listOf(doktor1, hasta1, hasta2)
    kisiler.forEach { println(it.bilgileriGoster()) }

    println("── selamVer() + gorevTanimi() ───────────────────────")
    listOf(doktor1, doktor2, hasta1).forEach {
        println("→ ${it.selamVer()}")
        println("   Görev: ${it.gorevTanimi()}")
    }

    println("\n── Encapsulation — Hasta Notları ────────────────────")
    hasta1.notEkle("Tansiyon ilacı başlandı")
    hasta1.notEkle("Kontrol randevusu verildi — 1 ay sonra")
    println("${hasta1.tamAd()} notları:")
    hasta1.notlariGetir().forEach { println("   $it") }

    println("\n── Encapsulation — Muayene Sayacı ───────────────────")
    doktor1.muayeneYap()
    doktor1.muayeneYap()
    println("${doktor1.tamAd()} toplam muayene: ${doktor1.muayeneSayisiniGetir()}")

    println("\n── Encapsulation — Durum Değişikliği ────────────────")
    println("Önce : ${if (doktor2.isAktif()) "Aktif" else "Pasif"}")
    doktor2.durumDegistir()
    println("Sonra: ${if (doktor2.isAktif()) "Aktif" else "Pasif"}")
    doktor2.muayeneYap()  

    println("\n── Randevu Sistemi ──────────────────────────────────")
    val r1 = Randevu(1, hasta1, doktor1, LocalDate.now(), "Kalp ritim kontrolü")
    val r2 = Randevu(2, hasta2, doktor1, LocalDate.now().plusDays(3), "Genel kontrol")
    r1.tamamla()
    r2.tamamla()

    println("\n${hasta1.tamAd()} randevu geçmişi:")
    hasta1.randevuGecmisiniGetir().forEach { println("   📋 $it") }

    println("\n── Inheritance — toString() (Kisi'den miras) ────────")
    listOf(doktor1, doktor2, hasta1, hasta2).forEach { println("   $it") }

    println("\n${"═".repeat(50)}")
    println("📊 Özet → Doktor: 2 | Hasta: 2 | Randevu: 2")
}
