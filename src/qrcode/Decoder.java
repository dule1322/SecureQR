package qrcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import crypto.AES;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Decoder {

    public void decode(AES crypt, String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            Logger.getLogger(Decoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (image == null) {
            return;
        }

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        QRCodeReader reader = new QRCodeReader();

        Result result = null;
        try {
            result = reader.decode(bitmap);
        } catch (NotFoundException | ChecksumException | FormatException ex) {
            Logger.getLogger(Decoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (result == null) {
            return;
        }

        byte[] bytes = Base64.getDecoder().decode(result.getText());
        
        byte[] ivBytes = Arrays.copyOfRange(bytes, 0, 16);
        byte[] encrypted = Arrays.copyOfRange(bytes, 16, bytes.length);
        
        String message = new String(crypt.decrypt(ivBytes, encrypted));
        System.out.println(message);
    }

}
