import java.io.*;

 class SpeechToText {

    public static String toText() {

        String s = null, str = "";
        //StringBuffer sb = new StringBuffer("");
 
        try {
            
        // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python speech.py");
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
           // System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                str += s;
                //System.out.println(s);
            }
            
            // read any errors from the attempted command
           // System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                //System.out.println(s);
            }
            //System.out.println(str);
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
        return str;
    }
}