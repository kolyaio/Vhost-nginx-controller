import java.util.*;
import java.io.*;
public class vhost {
    public static final String PATH_DEFAULT = "/home/nikolai/web/";
    public static final String PATH_DEFAULT_CONF = "/home/nikolai/web/conf/vhost/";

    static Scanner input = new Scanner(System.in);
    public static void createPublicFolder(String folderName) {
        String folder_path = PATH_DEFAULT + folderName + "/public/";
        try {
            boolean success = (new File(folder_path)).mkdir();
            if(success) {
                System.out.println("Direcetory created: " + folder_path);
            }
            else {
                System.out.println("Directory creation failed");
            }
        }
        catch(Exception e) {
            System.err.println("Error:" + e.getMessage());
        }
    }
    public static void createFolder(String folderName) {
        String folder_path = PATH_DEFAULT + folderName;
        try {
            boolean success = (new File(folder_path)).mkdir();
            if(success) {
                System.out.println("Direcetory created: " + folder_path);
            }
            else {
                System.out.println("Directory creation failed");
            }
            createPublicFolder(folderName);
        }
        catch(Exception e) {
            System.err.println("Error:" + e.getMessage());
        }
    }
    public static void createVhost(String vhostName) {
        String tmpl_file = PATH_DEFAULT + "/conf/vhost.tmpl";
        String vhost_file = PATH_DEFAULT_CONF + vhostName;
        ArrayList<String> lines = new ArrayList<String>();
        String line = null;
        try {
            File vhostTmpl = new File(tmpl_file);
            FileReader fr = new FileReader(vhostTmpl);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null)
            {
                if(line.contains("{website}")){
                    line =  line.replace("{website}", vhostName);
                }
                lines.add(line);
            }
            fr.close();
            br.close();

            File vhostFile = new File(vhost_file);
            if(!vhostFile.exists()) {
                vhostFile.createNewFile();
            }

            FileWriter fw  = new FileWriter(vhostFile);
            BufferedWriter out =  new BufferedWriter(fw);
            for(String s : lines)
            {
                out.write(s);
                out.write("\n");
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f:files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
    public static void removeFolder(String folderName) {
        String folder_path = PATH_DEFAULT + folderName;

        try {
            File folder = new  File(folder_path);
            deleteFolder(folder);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeVhost(String vhostFile) {
        String vhost_path = PATH_DEFAULT_CONF + vhostFile;

        try {
            boolean success  =  (new  File(vhost_path).delete());

            if(success) {
                System.out.println("File is deleted.");
            }
            else
            {
                System.out.println("Cannot delete the directory.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void nginxRestart() {
        try {
            Process pr = Runtime.getRuntime().exec("service nginx restart");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        if(args.length != 0) {
            if(args[0].equals("create")) {
                createFolder(args[1]);
                createVhost(args[1]);
                System.out.println("Creates vhost");
            }
            if(args[0].equals("remove")) {
                removeFolder(args[1]);
                removeVhost(args[1]);
                System.out.println("remove vhost");
            }
        }
        nginxRestart();
    }
}