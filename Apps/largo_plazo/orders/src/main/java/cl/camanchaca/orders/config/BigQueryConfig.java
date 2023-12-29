package cl.camanchaca.orders.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
public class BigQueryConfig {

    @Value("${bigquery.project-id}")
    private String projectId;

    @Value("${bigquery.client-id}")
    private String clientId;

    @Value("${bigquery.client-email}")
    private String clientEmail;

    @Value("${bigquery.private-key}")
    private String privateKeyString;

    @Bean
    public BigQuery bigQuery() throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKeyString = privateKeyString.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "")
                .replace("\\n", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Credentials credentials = ServiceAccountCredentials.newBuilder()
                .setProjectId(projectId)
                .setClientId(clientId)
                .setClientEmail(clientEmail)
                .setPrivateKey(privateKey)
                .build();
        return BigQueryOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }

}

