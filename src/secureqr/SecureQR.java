package secureqr;

import crypto.AES;
import qrcode.Decoder;
import qrcode.Encoder;

public class SecureQR {
    
    public static void main(String[] args) {
        AES crypt = new AES(16, "kljuc".getBytes());
        
        String poruka = "Dusko car";
        
        Encoder encoder = new Encoder(poruka.getBytes());
        encoder.encode(crypt, "C:\\Users\\Dusko\\Desktop\\qrcode.png");
        
        Decoder decoder = new Decoder();
        decoder.decode(crypt, "C:\\Users\\Dusko\\Desktop\\qrcode.png");
    }
    
}
