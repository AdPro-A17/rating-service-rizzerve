Penjelasan Pattern:
Observer Pattern, sebagaimana dijelaskan, memungkinkan komponen yang “mengamati” data untuk menerima notifikasi otomatis ketika data tersebut berubah.

Mengapa Dipilih:
Saat user memberikan atau mengubah rating, nilai rata-rata pada menu harus diperbarui secara langsung. Dengan pattern ini, komponen tampilan yang menampilkan average rating bisa langsung mendapat update saat ada perubahan pada data rating, sehingga menjaga konsistensi informasi yang tampil.

High Level Networking: Menggunakan Rest API

# Individual Submit: Kevin Yehezkiel Manurung - 2206826974
## 1. Component Diagram - Rating Service
![Component Diagram](image/rizzerve-a17-rating-service-component-diagram.png)

## 2. Code Diagram - Rating Service
![Component Diagram](image/rizzerve-a17-rating-service-code-diagram.png)

1. Modular dan Separation of Concerns
   Komponen dipisahkan berdasarkan tanggung jawab: Controller, Service, Repository, Model, Observer.



2. Service-Oriented + Observer Pattern
   RatingServiceImpl mengimplementasikan RatingSubject untuk mendukung notifikasi otomatis (misalnya ke sistem rekomendasi).


3. Autentikasi Terpisah (Reusability & Security)
   AuthenticationService dikelola terpisah sebagai eksternal sistem (microservice/shared module), sehingga rating bisa mengandalkan token dan user ID tanpa menduplikasi logika autentikasi.

   User (Frontend) → RatingController

Cara Kerja:

User (Frontend) → RatingController
- Frontend mengirimkan permintaan HTTP (POST/GET/PUT/DELETE) ke RatingController.

RatingController

- Mengekstrak token dari header request.

- Memanggil AuthenticationService untuk memverifikasi token dan mengambil userId.

- Memanggil RatingServiceImpl sesuai kebutuhan (save, getAll, getById, dll.).

RatingServiceImpl

- Bertanggung jawab atas logika bisnis rating (validasi data, penghitungan, pemanggilan repository).

- Menyimpan/mengambil data menggunakan RatingRepository.

- Jika ada perubahan rating, akan memanggil notifyObservers().

RatingRepository

- Menjalankan operasi database seperti save, findAll, delete, findByItemId, dll.

- Terkoneksi langsung dengan RatingDatabase.

RatingObserver

- Komponen lain (misalnya sistem rekomendasi atau notifikasi) yang mendaftar ke RatingServiceImpl akan menerima notifikasi saat ada perubahan data rating.