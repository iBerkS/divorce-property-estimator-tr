# CLAUDE.md — Divorce Property Estimator (TR)

## Proje Amacı

Türk Medeni Kanunu'nun edinilmiş mallara katılma rejimine (TMK m.218 vd.) göre
boşanmada mal paylaşımını yaklaşık hesaplayan masaüstü Java uygulaması.
Akademik/portfolyo projesi. Hukuki bağlayıcılığı yoktur.

---

## Tech Stack

- **Java 17** — dil seviyesi (pattern matching `instanceof`, switch expression vb.)
- **Java Swing** — GUI (build tool yok, saf Java projesi)
- **Build/IDE:** IntelliJ IDEA — `.iml` ve `out/` dizini `.gitignore`'da

---

## Nasıl Çalıştırılır

Build tool yok; saf classpath projesi:

1. IntelliJ IDEA'da aç
2. `src/` kaynak dizini olarak işaretle (Sources Root)
3. `src/com/divorce/estimator/Main.java` → Run

Komut satırından (proje kökünde):
```
javac -d out -sourcepath src src/com/divorce/estimator/Main.java
java -cp out com.divorce.estimator.Main
```

---

## Dosya Yapısı

```
src/com/divorce/estimator/
├── Main.java                      # Giriş noktası — SwingUtilities.invokeLater ile MainFrame başlatır
├── model/
│   ├── Asset.java                 # Varlık (mal): ad, tür, sahip, değer, borç; getNetValue() = değer - borç
│   ├── PropertyType.java          # Enum: ACQUIRED (edinilmiş) | PERSONAL (kişisel)
│   └── Spouse.java                # Enum: A | B
├── service/
│   ├── Calculator.java            # Tek metod: calculate(List<Asset>) → CalculationResult
│   └── CalculationResult.java     # VO: aNet, bNet, totalNet, equalShare, aReceivable, bReceivable
└── ui/
    ├── MainFrame.java             # Ana pencere: tablo + butonlar + sonuç alanı
    ├── AssetDialog.java           # Modal: mal ekleme formu
    └── DisclaimerDialog.java      # Modal: yasal uyarı — kabul edilmezse uygulama kapanır
```

---

## Mimari

Üç katmanlı basit OOP:

```
ui  ──►  service  ──►  model
         Calculator     Asset
         CalculationResult  PropertyType
                        Spouse
```

- **model:** saf POJO, hiç UI/servis bağımlılığı yok
- **service:** `Calculator` sadece `List<Asset>` alır, UI'dan bağımsız; test edilebilir
- **ui:** Swing bileşenleri; Türkçe etiketler inline (enum renderer + `toTurkish()` helper)

---

## İş Mantığı — Calculator

1. `PERSONAL` mallar hesaplamaya dahil edilmez.
2. Her malın net değeri = `değer - borç`; negatif ise 0 kabul edilir (v1.0 basitlik kararı).
3. `totalNet = aNet + bNet`
4. `equalShare = totalNet / 2`
5. `aReceivable = equalShare - aNet` (pozitif ise Eş A alacaklı, negatif ise Eş B alacaklı)

---

## Bilinen Eksikler (Sonraki Milestone)

- `MainFrame`'de `calculateButton` tanımlanmış ama **ActionListener eklenmemiş** — hesap sonucu `resultArea`'ya henüz yazdırılmıyor.
- Mal listesi yalnızca `DefaultTableModel`'da string olarak tutuluyor; `Calculator` için ayrı bir `List<Asset>` tutulmuyor. Hesap entegrasyonu için paralel bir liste veya tablonun model katmanına bağlanması gerekiyor.
- Rapor dışa aktarma (PDF/CSV) planlanmış, henüz yok.

---

## Kod Konvansiyonları

- **Dil:** Tüm UI metinleri Türkçe; kod (sınıf/metod/değişken adları) İngilizce.
- **Enum görüntüleme:** Enum değerlerini Türkçe göstermek için `DefaultListCellRenderer` veya `toTurkish()` kullan; enum adını doğrudan UI'a yazma.
- **Türk ondalık ayracı:** Sayı parse ederken `s.replace(',', '.')` — TR klavye alışkanlığı için `AssetDialog.parseDouble()`'a bakılabilir.
- **Negatif net değer:** `Calculator`'da 0'a yuvarlanıyor — bilinçli bir v1.0 kararı; değiştirirken `CalculationResult` yorumunu güncelle.
- **Swing iş parçacığı:** Her zaman `SwingUtilities.invokeLater` içinde başlat; arka plan hesabı yokken EDT güvenli.
- **Yorum:** Yalnızca neden açık değilse (alan hukuku kararları, platforma özgü workaround).

---

## Dikkat Edilmesi Gereken Noktalar

- `DisclaimerDialog` `DO_NOTHING_ON_CLOSE` — pencere kapat düğmesi devre dışı; kullanıcı ya kabul eder ya da uygulamayı task manager'dan kapatır. Bu kasıtlı bir hukuki zorunluluk.
- `MainFrame` constructor'ı `DisclaimerDialog`'u gösterir ve `isAccepted() == false` ise `System.exit(0)` çağırır — `setVisible(true)` çağrılmadan önce gerçekleşir.
- Şu an build sistemi yok (Maven/Gradle); bağımlılık eklenirse bir build aracı benimsenmeli.
- Java 17 `instanceof` pattern matching (`if (value instanceof PropertyType pt)`) kullanılıyor — derleme hedefini 17'nin altına düşürme.
