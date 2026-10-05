# Login & Registration System

## Tujuan dan cakupan

Aplikasi Java 17 dan Spring Boot menyediakan registrasi serta login dengan email dan password. Requirement awal menyebut penyimpanan *in-memory*; sesuai instruksi terbaru, implementasi ini memakai PostgreSQL agar akun tetap tersimpan setelah aplikasi dimulai ulang. Fitur toko dan cabang yang sudah ada tetap tersedia.

## Perilaku yang diimplementasikan

- `POST /register` mendaftarkan email yang belum digunakan. Email disimpan dalam huruf kecil; password disimpan sebagai hash BCrypt, bukan teks asli.
- `POST /login` memvalidasi email dan password. Keduanya mengembalikan respons teks persis seperti contoh requirement.
- Request menerima `application/x-www-form-urlencoded` dan `application/json`.
- Email wajib ada, berformat valid, dan maksimal 254 karakter. Password registrasi wajib minimal 6 karakter. Password login wajib ada.
- Email duplikat ditolak dengan HTTP 409, termasuk bila dua registrasi bersamaan. Kredensial login yang salah ditolak dengan HTTP 401. Input yang tidak valid ditolak dengan HTTP 400.
- Endpoint lama `POST /api/auth/register` dan `POST /api/auth/login` tetap tersedia. Login lewat endpoint lama mengembalikan JWT untuk memakai API lama yang memerlukan autentikasi.

## Kontrak API

| Endpoint | Input | Sukses | Error yang umum |
| --- | --- | --- | --- |
| `POST /register` | `email`, `password` | HTTP 200, `User registered successfully.` | HTTP 400 atau 409 |
| `POST /login` | `email`, `password` | HTTP 200, `Login successful` | HTTP 400 atau 401 |
| `POST /api/auth/register` | `email`, `password` | HTTP 200, `User registered successfully.` | HTTP 400 atau 409 |
| `POST /api/auth/login` | `email`, `password` | HTTP 200, JSON `{"token":"..."}` | HTTP 400 atau 401 |

Contoh request form:

```http
POST /register HTTP/1.1
Content-Type: application/x-www-form-urlencoded

email=test%40example.com&password=mypassword
```

Contoh request JSON:

```http
POST /login HTTP/1.1
Content-Type: application/json

{"email":"test@example.com","password":"mypassword"}
```

Contoh error:

```json
{"error":"Invalid email or password"}
```

Error validasi berupa JSON dengan nama field sebagai kunci, misalnya `{"email":"Email must be valid"}`.

## PostgreSQL dan cara menjalankan

1. Sediakan PostgreSQL dan database yang sesuai dengan `spring.datasource.url` pada `src/main/resources/application.properties`. Konfigurasi username dan password database yang sudah ada tidak diubah oleh pekerjaan ini.
2. Jalankan aplikasi dengan `./mvnw spring-boot:run` di macOS/Linux atau `.\mvnw.cmd spring-boot:run` di Windows. Java 17 dan Maven Wrapper diperlukan.
3. Jalankan pengujian tanpa koneksi database dengan `./mvnw test` atau `.\mvnw.cmd test`.

JDBC URL, username, dan password dapat diberikan melalui environment variable Spring Boot `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, dan `SPRING_DATASOURCE_PASSWORD`. Jangan menaruh kredensial nyata di dokumentasi atau request contoh.

Hibernate memakai `ddl-auto=update` untuk membuat tabel baru `auth_users` tanpa menghapus tabel lama. Tabel ini memiliki email unik dan kolom hash password. Tabel `users` lama dengan field `username` tidak dimigrasikan otomatis; akun lama perlu didaftarkan ulang melalui email. Pembuatan data contoh toko/cabang tidak lagi berjalan otomatis saat startup.

## Verifikasi

Pengujian otomatis mencakup request form dan JSON, respons sukses, validasi input, email duplikat, kegagalan login, normalisasi email, dan penyimpanan hash BCrypt. Pengujian memakai mock repository dan tidak membaca atau mengubah database yang ada. Verifikasi terhadap PostgreSQL sungguhan perlu lingkungan uji yang terpisah.

## Catatan penggunaan AI

Perubahan dibatasi pada requirement autentikasi, penyesuaian PostgreSQL yang diminta, dan pengujian terkait. Hasil AI ditinjau melalui pemeriksaan kode dan pengujian otomatis; status pengujian aktual dicatat dalam laporan perubahan.
