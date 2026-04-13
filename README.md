# 🏥 Kotlin Hastane Sistemi — 4 Temel OOP

Kotlin'deki 4 temel OOP kavramını hastane senaryosuyla gösteren örnek proje.

---

## 🚀 Çalıştırma

```bash
kotlinc HastaneSistemi.kt -include-runtime -d hastane.jar
java -jar hastane.jar
```

---

## 📖 Satır Satır Açıklama

---

### 1. Abstraction — Soyutlama
> **Satır 12–22** → `abstract class Kisi`

`Kisi` sınıfı tüm kişi türleri için bir şablon görevi görür. İçindeki `bilgileriGoster()` ve `gorevTanimi()` fonksiyonları **abstract** tanımlanmıştır — yani gövdeleri yoktur, sadece bir söz verilir. Bu fonksiyonları kim kullanmak isterse kendi içeriğini yazmak zorundadır.

`tamAd()` ve `selamVer()` ise **open** olarak tanımlanmıştır; alt sınıflar isterse bunları yeniden yazabilir, istemezse olduğu gibi kullanabilir.

```kotlin
// Satır 17–18: Gövdesiz, alt sınıf doldurmak zorunda
abstract fun bilgileriGoster(): String
abstract fun gorevTanimi(): String

// Satır 20: Hazır, ama override edilebilir
open fun tamAd(): String = "$ad $soyad"
```

---

### 2. Inheritance — Kalıtım
> **Satır 30** → `class Doktor(...) : Kisi(id, ad, soyad, yas)`
> **Satır 72** → `class Hasta(...) : Kisi(id, ad, soyad, yas)`

Her iki sınıf da `: Kisi(...)` yazılarak `Kisi`'den türetilmiştir. Bu sayede `id`, `ad`, `soyad`, `yas` alanlarını ve `toString()` fonksiyonunu tekrar yazmak gerekmez; otomatik olarak miras alınır.

```kotlin
// Satır 30: Doktor, Kisi'nin tüm alanlarını miras alır
class Doktor(id: Int, ad: String, ...) : Kisi(id, ad, soyad, yas)

// Satır 22: toString() Kisi'de tanımlı → Doktor ve Hasta otomatik kullanır
override fun toString(): String = "[#$id] ${tamAd()} ($yas yaş)"
```

`main` içindeki **Satır 155–157** bölümünde `toString()` çağrılır; her iki alt sınıf da bu fonksiyonu kendi için kullanır.

---

### 3. Polymorphism — Çok Biçimlilik
> **Satır 38** → `override fun tamAd()` (Doktor versiyonu)
> **Satır 43** → `override fun bilgileriGoster()` (Doktor versiyonu)
> **Satır 85** → `override fun bilgileriGoster()` (Hasta versiyonu)
> **Satır 123–125** → `val kisiler: List<Kisi> = listOf(doktor1, hasta1, hasta2)`

Aynı fonksiyon adı, farklı sınıflarda farklı davranır. `bilgileriGoster()` çağrıldığında `Doktor` kendi kutusunu, `Hasta` kendi kutusunu basar.

`main` içindeki `List<Kisi>` listesi bunu en iyi şekilde gösterir: liste tipi `Kisi` olmasına rağmen, `forEach` döngüsünde her nesne **kendi** `bilgileriGoster()` fonksiyonunu çalıştırır.

```kotlin
// Satır 123–125: Tip Kisi ama davranış her nesneye özel
val kisiler: List<Kisi> = listOf(doktor1, hasta1, hasta2)
kisiler.forEach { println(it.bilgileriGoster()) }  // her biri farklı çıktı verir
```

`selamVer()` ve `gorevTanimi()` da aynı şekilde; **Satır 128–131** bölümünde Doktor ve Hasta için farklı metinler üretilir.

---

### 4. Encapsulation — Kapsülleme
> **Satır 35–36** → `private var aktif`, `private var muayeneSayisi`
> **Satır 61–68** → `fun durumDegistir()`, `fun muayeneYap()`, `fun muayeneSayisiniGetir()`
> **Satır 75–76** → `private val notlar`, `private val randevuGecmisi`
> **Satır 96–103** → `fun notEkle()`, `fun notlariGetir()`, `fun randevuEkle()`

`private` ile işaretlenen alanlar sınıf dışından doğrudan görülemez ve değiştirilemez. Dışarıdan erişim yalnızca kontrollü metotlar aracılığıyla yapılır:

- `aktif` → dışarıdan okunamaz; **Satır 62** `durumDegistir()` ile değiştirilir, **Satır 63** `isAktif()` ile okunur.
- `muayeneSayisi` → dışarıdan artırılamaz; **Satır 65** `muayeneYap()` çağrıldığında kontrol yapılarak artar.
- `notlar` → dışarıdan eklenemez; **Satır 96** `notEkle()` ile yazılır, **Satır 100** `notlariGetir()` ile **salt-okunur kopya** döner — orijinal liste korunur.

```kotlin
// Satır 35: private → dışarıdan erişilemez
private var aktif: Boolean = true

// Satır 62: tek değiştirme yolu
fun durumDegistir() { aktif = !aktif }

// Satır 100: kopya döner, orijinal korunur
fun notlariGetir(): List<String> = notlar.toList()
```

---

## 🖥️ Örnek Çıktı

```
🏥  HASTANE YÖNETİM SİSTEMİ
══════════════════════════════════════════════════

── bilgileriGoster() — Polymorphism ─────────────────
👨‍⚕️ DOKTOR | Ad: Dr. Mehmet Yılmaz | Sicil: SCL-001 | Uzmanlık: Kardiyoloji | Poliklinik: Kalp Polikliniği | Muayene: 0 | Durum: Aktif
🧑 HASTA | Ad: Zeynep Öztürk | TC: 11122233344 | Kan: A+ | Kronik: Hipertansiyon | Kayıt: 2026-04-14 | Not: 0 | Randevu: 0
🧑 HASTA | Ad: Murat Güneş | TC: 22233344455 | Kan: B- | Kronik: Yok | Kayıt: 2026-04-14 | Not: 0 | Randevu: 0

── Encapsulation — Hasta Notları ────────────────────
📝 Not eklendi: Tansiyon ilacı başlandı
📝 Not eklendi: Kontrol randevusu verildi — 1 ay sonra

── Encapsulation — Durum Değişikliği ────────────────
Önce : Aktif
Sonra: Pasif
⚠️  Dr. Ayşe Kaya şu an aktif değil.

── Inheritance — toString() (Kisi'den miras) ────────
   [#1] Dr. Mehmet Yılmaz (48 yaş)
   [#2] Dr. Ayşe Kaya (39 yaş)
   [#101] Zeynep Öztürk (34 yaş)
   [#102] Murat Güneş (52 yaş)
```

---

*Kotlin 1.8+ · JDK 11+*
