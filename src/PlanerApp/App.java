package PlanerApp;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) {
        try{
        Main.main(args);}
        catch (Exception e){System.out.println(e);}

    }
    public Path getPath(){

//        System.out.println("Class:" + this.getClass());
//        System.out.println("ProtecDomain:" + this.getClass().getProtectionDomain());
//        System.out.println("CodeSource:" + this.getClass().getProtectionDomain().getCodeSource());
//        System.out.println("Location:" + this.getClass().getProtectionDomain().getCodeSource().getLocation());
//        System.out.println("getPath = " + this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Path p = Path.of("");
        try {
            p = Path.of(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        }catch (URISyntaxException e){System.out.println(e.getStackTrace());}
//       String s = "";
//        try {
//             s = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toString();
//        }catch (URISyntaxException e){System.out.println(e);}
//        return s;
        return p.getParent();
    }
}
