    package com.smartict.ProjectPoll.config;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

    @Configuration
    public class AppConfig {

        @Value("${active-directory.domain}")
        private String activeDirectoryDomain;

        @Value("${active-directory.url}")
        private String activeDirectoryUrl;

        @Bean
        public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
            ActiveDirectoryLdapAuthenticationProvider provider =
                    new ActiveDirectoryLdapAuthenticationProvider(activeDirectoryDomain, activeDirectoryUrl);
            provider.setConvertSubErrorCodesToExceptions(true);
            provider.setUseAuthenticationRequestCredentials(true);
            return provider;
        }


    }
