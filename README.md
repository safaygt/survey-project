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


3. ⚙️ application.properties dosyasını oluşturun:

spring.application.name=ProjectPoll
spring.datasource.url=jdbc:postgresql://localhost:5432/pollDB
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update 
spring.jpa.show-sql=true 

jwt.secret=Gv3R2sNwPbd7k7U3h7vWkT+NkLWJHzBlZBIZFwS+NxY= 
jwt.expiration=3600000

survey.security.cors.allowed-hosts[0]=http://localhost:5173

# 🔐 Active Directory ayarları
activeDirectory.domain=example.local
activeDirectory.url=ldap://example.ip.address/


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


