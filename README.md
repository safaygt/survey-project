# 🚀 Survey Project Backend

Bu proje, **kurum içi anket yönetim sistemi** olan **ProjectPoll** uygulamasının **sunucu (backend)** tarafını oluşturmak için **Spring Boot** ile geliştirilmiştir.

---

## 📌 Proje Hakkında

**ProjectPoll**, şirket çalışanlarının anket oluşturmasına, yanıtlamasına ve yönetmesine olanak tanıyan bir web uygulamasıdır.  
Bu backend projesi, Active Directory entegrasyonu, JWT ile güvenli oturum yönetimi ve rol tabanlı erişim kontrolü sunar.

---

## 🎯 Özellikler

✅ Active Directory ile kurum içi kullanıcı girişi  
✅ JWT ile kullanıcı kimlik doğrulaması  
✅ Rol tabanlı erişim kontrolü (admin/kullanıcı)  
✅ Anket oluşturma, düzenleme, silme (sadece admin)  
✅ Sorular ve yanıt seçeneklerini yönetme  
✅ Anket yanıtlama ve sonuçları saklama  
✅ RESTful API yapısı  
✅ CORS yapılandırması

---

## 🛠️ Kullanılan Teknolojiler

- ☕ Java 17  
- 🌱 Spring Boot  
- 🔐 Spring Security (JWT ile)  
- 🗂 Spring Data JPA  
- 🗄️ PostgreSQL  
- 🧠 ModelMapper  
- 📋 Lombok  
- 📦 Gradle  
- 🧩 LdapTemplate (Active Directory için)

---

## ⚙️ Kurulum

Projeyi yerel ortamınızda çalıştırmak için aşağıdaki adımları izleyin:

1. 🔽 Depoyu klonlayın:

   ```bash
   git clone https://github.com/safaygt/survey-project.git

  
2. 📁 Proje dizinine geçin:

   ```bash
    cd survey-project


<ul>
  <li>
    <strong>📦 Veritabanı Ayarları</strong>
    <ul>
      <li><code>spring.application.name=ProjectPoll</code></li>
      <li><code>spring.datasource.url=jdbc:postgresql://localhost:5432/pollDB</code></li>
      <li><code>spring.datasource.username=postgres</code></li>
      <li><code>spring.datasource.password=postgres</code></li>
      <li><code>spring.datasource.driver-class-name=org.postgresql.Driver</code></li>
    </ul>
  </li>
  <li>
    <strong>🔄 Hibernate Ayarları</strong>
    <ul>
      <li><code>spring.jpa.hibernate.ddl-auto=update</code></li>
      <li><code>spring.jpa.show-sql=true</code></li>
    </ul>
  </li>
  <li>
    <strong>🔐 JWT Ayarları</strong>
    <ul>
      <li><code>jwt.secret=Gv3R2sNwPbd7k7U3h7vWkT+NkLWJHzBlZBIZFwS+NxY=</code></li>
      <li><code>jwt.expiration=3600000</code></li>
    </ul>
  </li>
  <li>
    <strong>🌐 CORS Ayarları</strong>
    <ul>
      <li><code>survey.security.cors.allowed-hosts[0]=http://localhost:5173</code></li>
    </ul>
  </li>
  <li>
    <strong>🧩 Active Directory Ayarları</strong>
    <ul>
      <li><code>activeDirectory.domain=example.local</code></li>
      <li><code>activeDirectory.url=ldap://example.ip.address/</code></li>
    </ul>
  </li>
</ul>
5. ▶️ Uygulamayı çalıştırın:

   ```bash
   ./gradlew bootRun

   
🧪 Kullanım
1. Tarayıcıdan frontend uygulamasını açın

2. Active Directory hesabınızla giriş yapın

3. Rolünüze göre anket oluşturun veya yanıtlayın

4. Anket sonuçlarını analiz edin



🌐 Frontend Projesi

Backend ile birlikte çalışması gereken React tabanlı frontend projesine aşağıdaki bağlantıdan ulaşabilirsiniz:

📎 [smartICT_frontend → GitHub](https://github.com/safaygt/smartICT_frontend.git)

☝️ Not: Frontend uygulaması çalışmadan önce backend sunucusunun ayağa kaldırılmış olması gereklidir. Aksi takdirde API bağlantıları başarısız olacaktır.



---



🤝 Katkıda Bulunma
Projeye katkıda bulunmak isterseniz aşağıdaki adımları izleyebilirsiniz:

Bu repoyu fork edin

Yeni bir branch oluşturun:
   ```bash
  git checkout -b feature/YeniOzellik
  ```

Geliştirmelerinizi yapın

Commit atın:
   ```bash
  git commit -m 'Yeni özellik eklendi'
   ```
Değişiklikleri gönderin:
   ```bash
  git push origin feature/YeniOzellik
  ```


---


> Geliştiren: [@safaygt](https://github.com/safaygt)  

> 💡 Sorularınız ya da önerileriniz için PR veya issue açabilirsiniz!


