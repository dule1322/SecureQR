package qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import crypto.AES;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Encoder {
    
    private final QRCodeWriter writer;
    private final byte[] data;

    public Encoder(byte[] data) {
        this.writer       = new QRCodeWriter();
        this.data         = data;
    }

    public Encoder(String characterSet, byte[] data) {
        this.writer       = new QRCodeWriter();
        this.data         = data;
    }
    
    public void encode(AES crypt, String filePath) {
        byte[] encrypted = crypt.encrypt(data);
        byte[] ivBytes   = crypt.getIvSpec().getIV();
        
        byte[] bytes = new byte[ivBytes.length + encrypted.length];

        System.arraycopy(ivBytes, 0, bytes, 0, ivBytes.length);
        System.arraycopy(encrypted, 0, bytes, ivBytes.length, encrypted.length);
        
        String message = Base64.getEncoder().encodeToString(bytes);
        
        BitMatrix encoded = null;
        try {
            encoded = writer.encode(message, BarcodeFormat.QR_CODE, 350, 350);
        } catch (WriterException ex) {
            Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (encoded == null) {
            return;
        }
        
        Path path = FileSystems.getDefault().getPath(filePath);
        try {
            MatrixToImageWriter.writeToPath(encoded, "PNG", path);
        } catch (IOException ex) {
            Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
