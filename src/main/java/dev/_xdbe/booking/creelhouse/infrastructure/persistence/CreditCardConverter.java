package dev._xdbe.booking.creelhouse.infrastructure.persistence;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class CreditCardConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        // Step 7a: Encrypt the PAN before storing it in the database
        return CryptographyHelper.encryptData(attribute);
        // Step 7a: End of PAN encryption
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // Step 7b: Decrypt the PAN when reading it from the database
        String pan = CryptographyHelper.decryptData(dbData);
        // Step 7b: End of PAN decryption
        return panMasking(pan);
    }

    private String panMasking(String pan) {
        // Step 6:
        if (pan == null || pan.length() <= 8) return pan;
        String first4 = pan.substring(0, 4);
        String last4 = pan.substring(pan.length() - 4);
        String middle = "*".repeat(pan.length() - 8);
        return first4 + middle + last4;
        // Step 6: End
    }
}
