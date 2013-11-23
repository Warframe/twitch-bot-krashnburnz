package View;

import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class FileDownloaderNEW extends JFrame {
  private static final long serialVersionUID = 1L;

  public static void download(String a1, String a2, boolean showUI, boolean exit)
    throws Exception
  {

    String site = a1;
    String filename = a2;
    JFrame frm = new JFrame("Download Progress");
    JProgressBar current = new JProgressBar(0, 100);
    JProgressBar DownloadProg = new JProgressBar(0, 100);
    JLabel downloadSize = new JLabel();
    current.setSize(50, 50);
    current.setValue(43);
    current.setStringPainted(true);
    frm.add(downloadSize);
    frm.add(current);
    frm.add(DownloadProg);
    frm.setVisible(showUI);
    frm.setLayout(new GridLayout(1, 3, 5, 5));
    frm.pack();
    frm.setDefaultCloseOperation(3);
    try
    {
      URL url = new URL(site);
      HttpURLConnection connection = 
        (HttpURLConnection)url.openConnection();
      int filesize = connection.getContentLength();
      float totalDataRead = 0.0F;
      BufferedInputStream in = new      BufferedInputStream(connection.getInputStream());
      FileOutputStream fos = new FileOutputStream(filename);
      BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
      byte[] data = new byte[1024];
      int i = 0;
      while ((i = in.read(data, 0, 1024)) >= 0)
      {
        totalDataRead += i;
        float prog = 100.0F - totalDataRead * 100.0F / filesize;
        DownloadProg.setValue((int)prog);
        bout.write(data, 0, i);
        float Percent = totalDataRead * 100.0F / filesize;
        current.setValue((int)Percent);
        double kbSize = filesize / 1000;

        String unit = "kb";
        double Size;
        if (kbSize > 999.0D) {
          Size = kbSize / 1000.0D;
          unit = "mb";
        } else {
          Size = kbSize;
        }
        downloadSize.setText("Filesize: " + Double.toString(Size) + unit);
      }
      bout.close();
      in.close();
      System.out.println("Took " + System.nanoTime() / 1000000000L / 10000L + "      seconds");
    }
    catch (Exception e)
    {
      JOptionPane.showConfirmDialog(
        null, e.getMessage(), "Error", 
        -1);
    } finally {
        if(exit = true){
            //System.exit(128);   
        }

    }
  }
}