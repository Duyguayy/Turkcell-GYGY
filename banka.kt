import java.util.UUID

data class Kullanici(
    val id: String,
    val kullaniciAdi: String,
    val sifre: String,
    var bakiye: Double,
    var krediPuani: Int = 0
)

val kullanicilar = mutableMapOf<String, Kullanici>()

val kurlar = mapOf(
    "TRY" to 1.0,
    "USD" to 32.0,
    "EUR" to 35.0
)

fun kayitOl(kullaniciAdi: String, sifre: String, baslangicBakiye: Double): String {

    if (kullaniciAdi.isBlank()) {
        throw IllegalArgumentException("Kullanıcı adı boş olamaz")
    }

    if (sifre.length < 4) {
        throw IllegalArgumentException("Şifre en az 4 karakter olmalı")
    }

    val id = UUID.randomUUID().toString()
    val yeniKullanici = Kullanici(id, kullaniciAdi, sifre, baslangicBakiye)

    kullanicilar[id] = yeniKullanici
    return id
}

fun girisYap(kullaniciAdi: String, sifre: String): Kullanici {
    val kullanici = kullanicilar.values.find { it.kullaniciAdi == kullaniciAdi }
        ?: throw Exception("Kullanıcı bulunamadı")

    if (kullanici.sifre != sifre) {
        throw Exception("Şifre hatalı")
    }

    return kullanici
}

fun paraYatir(kullanici: Kullanici, miktar: Double) {
    if (miktar <= 0) {
        throw IllegalArgumentException("Miktar pozitif olmalı")
    }

    kullanici.bakiye += miktar
    println("$miktar TL yatırıldı")
}

fun paraCek(kullanici: Kullanici, miktar: Double) {
    if (miktar <= 0) {
        throw IllegalArgumentException("Miktar pozitif olmalı")
    }

    if (kullanici.bakiye < miktar) {
        throw Exception("Yetersiz bakiye")
    }

    kullanici.bakiye -= miktar
    println("$miktar TL çekildi")
}

fun kurCevir(miktar: Double, from: String, to: String): Double {

    val fromKur = kurlar[from] ?: throw Exception("Geçersiz para birimi")
    val toKur = kurlar[to] ?: throw Exception("Geçersiz para birimi")

    val tlKarsiligi = miktar * fromKur
    return tlKarsiligi / toKur
}

fun krediPuaniHesapla(kullanici: Kullanici): Int {

    val puan = when {
        kullanici.bakiye > 100000 -> 800
        kullanici.bakiye > 50000 -> 700
        kullanici.bakiye > 10000 -> 600
        kullanici.bakiye > 1000 -> 500
        else -> 300
    }

    kullanici.krediPuani = puan
    return puan
}

fun kullaniciBilgisi(kullanici: Kullanici): String {
    return """
        Kullanıcı: ${kullanici.kullaniciAdi}
        Bakiye: ${kullanici.bakiye} TL
        Kredi Puanı: ${kullanici.krediPuani}
    """.trimIndent()
}

fun main() {

    println("=== Banka Sistemine Hoş Geldiniz ===")

    try {
        kayitOl("ahmet", "1234", 10000.0)
        println("Kullanıcı oluşturuldu")

        val kullanici = girisYap("ahmet", "1234")
        println("Giriş başarılı\n")

        paraYatir(kullanici, 2000.0)
        paraCek(kullanici, 1500.0)

        val usd = kurCevir(1000.0, "TRY", "USD")
        println("1000 TL = $usd USD")

        val puan = krediPuaniHesapla(kullanici)
        println("Kredi puanı: $puan")

        println("\n--- Hesap Bilgisi ---")
        println(kullaniciBilgisi(kullanici))

    } catch (e: Exception) {
        println("HATA: ${e.message}")
    }
}