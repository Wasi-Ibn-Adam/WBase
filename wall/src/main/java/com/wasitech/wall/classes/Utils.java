package com.wasitech.wall.classes;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.wasitech.wall.R;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static android.content.Context.BLUETOOTH_SERVICE;

public class Utils {
    private static final String TAG = "founder";

    public static class Img {
        // BITMAP
        public static Bitmap parse(Drawable drawable) {
            if (drawable == null) {
                return null;
            }
            Bitmap bitmap;
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        public static Bitmap parse(byte[] array) {
            if (array != null)
                return BitmapFactory.decodeByteArray(array, 0, array.length);
            return null;
        }

        public static Bitmap parse(String imgPath) {
            return BitmapFactory.decodeFile(imgPath);
        }

        public static Bitmap thumbNail(String path) {
            return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
        }

        public static Bitmap download(String url) {
            try {
                URL url1 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                connection.setDoInput(true);
                connection.connect();
                return BitmapFactory.decodeStream(connection.getInputStream());
            } catch (IOException e) {
                Issue.print(e, Img.class.getName());
            }
            return null;
        }

        // BYTES

        public static byte[] parseBytes(Bitmap map) {
            if (map != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                map.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            }
            return null;
        }

        //FILE

        public static File parseFile(Bitmap map) {
            File photo = Storage.CreateDataFile(Storage.DB, "temp", ".jpg");
            try {
                OutputStream out = new FileOutputStream(photo);
                map.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                Issue.print(e, Img.class.getName());
            }
            return photo;
        }

        public static File parseFile(byte[] bytes) {
            File photo = Storage.CreateDataFile(Storage.DB, "temp", ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(photo);
                fos.write(bytes);
                fos.close();
            } catch (IOException e) {
                Issue.print(e, Img.class.getName());
            }
            return photo;
        }

        public static File parseFile(Drawable drawable) {
            Bitmap map = parse(drawable);
            return parseFile(map);
        }

        public static File parseFile(Bitmap map, String filename) {
            File photo = Storage.CreateDataFile(Storage.DB, "temp", filename, ".jpg");
            try {
                OutputStream out = new FileOutputStream(photo);
                map.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                Issue.print(e, Img.class.getName());
            }
            return photo;
        }

        public static File parseFile(byte[] bytes, String filename) {
            File photo = Storage.CreateDataFile(Storage.DB, "temp", filename, ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(photo);
                fos.write(bytes);
                fos.close();
            } catch (IOException e) {
                Issue.print(e, Img.class.getName());
            }
            return photo;
        }

        public static File parseFile(Drawable drawable, String filename) {
            Bitmap map = parse(drawable);
            return parseFile(map, filename);
        }

        // BOOLEAN
        private boolean saveBitmap(Bitmap bitmap, File file) {
            try {
                OutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                return true;
            } catch (IOException e) {
                Issue.print(e, Img.class.getName());
                return false;
            }
        }
        // VOID

        public static void save(Context context, Bitmap bitmap) {
            File map = parseFile(bitmap);
            mediaScanner(context, map.getPath());
        }

        public static void save(Context context, Drawable drawable) {
            File file = parseFile(drawable);
            mediaScanner(context, file.getPath());
        }

        public static void save(Context context, byte[] bytes) {
            File file = parseFile(bytes);
            mediaScanner(context, file.getPath());
        }

        public static void save(Context context, Bitmap bitmap, String fileName) {
            File map = parseFile(bitmap, fileName);
            mediaScanner(context, map.getPath());
        }

        public static void save(Context context, Drawable drawable, String fileName) {
            File map = parseFile(drawable, fileName);
            mediaScanner(context, map.getPath());
        }

        public static void save(Context context, byte[] bytes, String fileName) {
            File map = parseFile(bytes, fileName);
            mediaScanner(context, map.getPath());
        }


        public static void mediaScanner(Context context, String filePath) {
            MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{filePath}, null, (path, uri) -> {
            });
        }

        public static void mediaScanner(Context context, String filePath, String success, String failed) {
            MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    if (path != null && new File(path).exists()) toasting(context, success);
                    else toasting(context, failed);
                }
            });
        }
    }

    public static class Internet {
        private static String data;
        private static int number = 1;

        @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
        private static boolean isConnected(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo temp = connectivityManager.getActiveNetworkInfo();
            if (temp != null && temp.isConnected()) {
                temp.getDetailedState();
                return true;
            } else {
                toasting(context, "Internet Not Connected");
                return false;
            }
        }

        @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
        public static boolean isInternetJson(Context context) {
            if (number == 1) {
                data = " try run";
            }
            if (isConnected(context)) {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(new JsonObjectRequest(Request.Method.GET, "https://jsonplaceholder.typicode.com/todos/" + (number), null,
                        response -> {
                            try {
                                data = response.getString("title");
                            } catch (JSONException e) {
                                Issue.print(e, Internet.class.getName());
                            }
                        }, Issue::Internet));
                if (number == 200) {
                    number = 1;
                } else {
                    number++;
                }
                if (data != null) return true;
                else toasting(context, "Make Sure You Have An Active Internet Connection.");
            }
            return false;
        }

        public static Intent intentGPS() {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            return poke;
        }

        public static boolean canToggleGPS(Context context) {
            PackageManager pacman = context.getPackageManager();
            PackageInfo pacInfo;
            try {
                pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
            } catch (PackageManager.NameNotFoundException e) {
                Issue.print(e, Internet.class.getName());
                return false;
            }
            if (pacInfo != null) {
                for (ActivityInfo actInfo : pacInfo.receivers) {
                    if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported) {
                        return true;
                    }
                }
            }

            return false; //default
        }
    }

    public static class Strings {

        /**
         * @return true if email is Valid
         * @implNote email will be in this format abc@stu.xy
         */
        public static boolean EmailValidator(String email) {
            String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
            return Pattern.compile(regex).matcher(email).matches();
        }

        /**
         * @return true if String 1 contains String 2 or vice versa
         */
        public static boolean contains(String s1, String s2) {
            return s2.contains(s1) || s1.contains(s2) || s1.equalsIgnoreCase(s2);
        }

        /**
         * @return true if String 1 containsIgnoreCase String 2 or vice versa
         */
        public static boolean containsIgnoreCase(String s1, String s2) {
            return s1.toLowerCase().contains(s2.toLowerCase())
                    || s2.toLowerCase().contains(s1.toLowerCase())
                    || s1.equalsIgnoreCase(s2);
        }

        /**
         * @return true if String 1 equals String 2 or vice versa
         */
        public static boolean equals(String s1, String s2) {
            return (s1 == null && s2 == null) || ((s1 != null && s2 != null) && (s1.trim().equals(s2.trim())));
        }

        /**
         * @param list ["as","as","asa","ssdd"]
         * @return "as,as,asa,ssdd"
         */
        public static String toString(ArrayList<String> list) {
            StringBuilder builder = new StringBuilder();
            for (String s : list) {
                builder.append(s).append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        }

        public static String sentenceCase(String str) {
            String[] token = str.split("\\.");
            StringBuilder builder = new StringBuilder();
            for (String s : token) {
                String line = s;
                String fc = line.charAt(0) + "";
                line = fc.toUpperCase() + line.substring(1) + ".";
                builder.append(line);
            }
            return builder.toString();
        }

        public static String titleCase(String str) {
            String[] token = str.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : token) {
                String line = s;
                String fc = line.charAt(0) + "";
                line = fc.toUpperCase() + line.substring(1) + " ";
                builder.append(line);
            }
            return builder.toString();
        }


    }

    public static class Numbers {
        /**
         * @return true if only numbers exist in string
         */
        public static boolean areOnlyNumbers(String text) {
            for (char t : text.toCharArray()) {
                if (t < '0' || t > '9')
                    return false;
            }
            return true;
        }

        /**
         * @return true if only valid contact number
         * @implNote can have max 2 dashes, 1 plus sign and rest are numbers
         */
        public static boolean isContactNumber(String str) {
            if (str.isEmpty()) return false;
            int plus = 0, dash = 0;
            if (str.charAt(0) == '+') {
                plus = 1;
            }
            for (char c : str.toCharArray()) {
                if (c < '0' || c > '9') {
                    if (c == '+') {
                        if (plus == 1)
                            plus--;
                        else
                            return false;
                    } else if (c == '-') {
                        if (dash <= 1)
                            dash++;
                        else
                            return false;
                    } else if (c != ' ')
                        return false;
                }

            }
            return true;
        }

        public static String numberFormat(String num) {
            if (num.length() <= 4)
                return num;
            return In_3_Format(num.substring(0, num.length() - 4)) + "-" + num.substring(num.length() - 4);
        }

        public static String number7Format(String num) {
            if (num.length() <= 7)
                return num;
            return In_3_Format(num.substring(0, num.length() - 7)) + "-" + num.substring(num.length() - 7);
        }

        public static String In_3_Format(String num) {
            String str = new StringBuilder(num).reverse().toString().trim();
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= str.length(); i++) {
                builder.append(str.charAt(i - 1));
                if (i == str.length() - 1)
                    if (str.length() % 3 == 1)
                        continue;
                if (i % 3 == 0 && i != str.length())
                    builder.append("-");
            }


            return builder.reverse().toString();
        }

        public static String getOnlyNumbers(String str) {
            if (str.isEmpty()) return "";
            StringBuilder builder = new StringBuilder();
            for (char c : str.toCharArray()) {
                if (c < '0' || c > '9')
                    continue;
                builder.append(c);
            }
            return builder.toString();
        }

    }

    public static class Time {

        /**
         * @return long to String default time format
         */
        public static String longToTime(long time) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            return cal.getTime().toString();
        }

        /**
         * @return time in millis to String
         * @implNote return format will be 'hh:mm:ss' or 'dd-mm-year hh:mm:ss' depending on millis
         */
        public static String millisToTime(long millis) {
            long ans = millis / 1000;
            return secToTime(ans);
        }

        /**
         * @return time in sec to String
         * @implNote return format will be 'hh:mm:ss' or 'dd-mm-year hh:mm:ss' depending on millis
         */

        public static String secToTime(long sec) {
            int s = seconds_60(sec * 1000);
            int m = minutes_60(sec * 1000);
            int h = hours_24(sec * 1000);
            if (sec < 86400) return getTime(h, m, s); // 86400 seconds in a day
            return time(sec * 1000);
        }

        /**
         * @return if (h > 0) -> hh:mm:ss<br>
         * else if (m > 0) -> mm:ss<br>
         * else -> ss
         */
        public static String getTime(int h, int m, int s) {
            if (h > 0)
                return Utils.Min2(h) + ":" + Utils.Min2(m) + ":" + Utils.Min2(s);
            if (m > 0)
                return Utils.Min2(m) + ":" + Utils.Min2(s);
            return Utils.Min2(s);
        }

        /**
         * @return hh:mm:ss
         */
        public static String getTimeString(int h, int m, int s) {
            return Utils.Min2(h) + ":" + Utils.Min2(m) + ":" + Utils.Min2(s);
        }

        /**
         * @return default date-time
         */
        public static String time(long time) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar.getTime().toString();
        }

        public static int seconds_60(long milli) {
            if (milli < 0)
                return 0;
            int t = (int) (milli / 1000);
            return t % 60;
        }

        public static int minutes_60(long milli) {
            if (milli < 0)
                return 0;
            int sec = (int) (milli / (1000 * 60));
            return sec % 60;
        }

        public static int hours_24(long milli) {
            if (milli < 0)
                return 0;
            int min = (int) (milli / (1000 * 60 * 60));
            return min % 24;
        }

        public static int hours_12(long milli) {
            if (milli < 0)
                return 0;
            int min = (int) (milli / (1000 * 60 * 60));
            return min % 12;
        }

        public static int day_7(long milli) {
            if (milli < 0)
                return 0;
            int h = (int) (milli / (1000 * 60 * 60 * 24));
            return h % 7;
        }

        public static int day_(long milli, int d) {
            if (milli < 0 || d < 0)
                return 0;
            int h = (int) (milli / (1000 * 60 * 60 * 24));
            return h % d;
        }

        public static int month_12(long milli) {
            if (milli < 0)
                return 0;
            int day = (int) (milli / (1000 * 60 * 60 * 24));
            return day % 12;
        }

        public static int month_(long milli, int m) {
            if (milli < 0)
                return 0;
            int day = (int) (milli / (1000 * 60 * 60 * 24));
            return day % m;
        }
    }

    public static class Size {

        public static String size(long bytes) {
            if (bytes <= 0)
                return "0 bytes";
            if (bytes < 1024)
                return bytes + " bytes";
            long kb = bytes / 1024;
            if (kb < 1024)
                return kb + " KB";
            long mb = kb / 1024;
            if (mb < 1024)
                return mb + "." + Utils.Min2(percent(1024, (int) kb % 1024)) + " Mbs";
            long gbs = mb / 1024;
            return gbs + "." + Utils.Min2(percent(1024, (int) mb % 1024)) + " Gbs";
        }

        public static long kbs_maxMb(long bytes) {
            return kbs_(bytes) % 1024;
        }

        public static long kbs_(long bytes) {
            return Math.max(0, bytes / 1024);
        }

        public static long mbs_(long bytes) {
            return Math.max(0, bytes / (1024 * 1024));
        }

        public static long mbs_maxGb(long bytes) {
            return mbs_(bytes) % (1024);
        }

        public static long gbs_(long bytes) {
            return Math.max(0, bytes / (1024 * 1024 * 1024));
        }

        public static long gbs_maxTb(long bytes) {
            return gbs_(bytes) % (1024);
        }

    }

    public static class Firebase {

        /**
         * @param s string of path
         * @return valid path
         * @see #firebasePath(String, String)
         */
        public static String firebasePath(String s) {
            if (s == null)
                return null;
            return s.replace(".", "")
                    .replace("#", "")
                    .replace("[", "")
                    .replace("]", "");
        }

        /**
         * @param s string of path
         * @param c replacment with restricted chars
         * @return valid path
         * @see #firebasePath(String)
         */
        public static String firebasePath(String s, String c) {
            if (s == null)
                return null;
            return s.replace(".", c)
                    .replace("#", c)
                    .replace("[", c)
                    .replace("]", c);
        }

    }

    public static class AudioVideo {
        public static final int AUD_STOPPED = 1;
        public static final int VID_STOPPED = 1;
        public static final int AUD_ERROR = -1;
        public static final int AUD_ALREADY = 0;
        public static final int AUD_PAUSE = 0;
        public static final int AUD_PLAY = 1;
        public static final int AUD_NEXT = 2;
        public static final int AUD_PREV = 3;
        public static final int AUD_STOP = 4;

        @SuppressLint("MissingPermission")
        public static void Vibrator(Context context, Long sec) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(sec, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(sec);
            }
        }

        public static int stopAudio(Context context) {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (am.isMusicActive()) {
                AudioManager.OnAudioFocusChangeListener focusChangeListener = i -> {
                };
                int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                am.abandonAudioFocus(focusChangeListener);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                    return AUD_STOPPED;
                else {
                    return AUD_ERROR;
                }
            } else {
                return AUD_ALREADY;
            }
        }

        public static void audioManager(Context context, int modes) {
            boolean yes = Build.MANUFACTURER.toLowerCase().contains("samsung");
            if (yes) {
                Intent i = null;
                switch (modes) {
                    case AUD_PLAY: {
                        i = new Intent("com.sec.android.app.music.musicservicecommand.play");
                        break;
                    }
                    case AUD_NEXT: {
                        i = new Intent("com.sec.android.app.music.musicservicecommand.next");
                        break;
                    }
                    case AUD_PAUSE: {
                        i = new Intent("com.sec.android.app.music.musicservicecommand.pause");
                        break;
                    }
                    case AUD_PREV: {
                        i = new Intent("com.sec.android.app.music.musicservicecommand.previous");
                        break;
                    }
                    case AUD_STOP: {
                        i = new Intent("com.sec.android.app.music.musicservicecommand.stop");
                        break;
                    }
                }
                if (i != null)
                    context.getApplicationContext().sendBroadcast(i);
            } else {
                Intent i = new Intent("com.android.music.musicservicecommand");
                switch (modes) {
                    case AUD_PLAY: {
                        i.putExtra("command", "play");
                        break;
                    }
                    case AUD_NEXT: {
                        i.putExtra("command", "next");
                        break;
                    }
                    case AUD_PAUSE: {
                        i.putExtra("command", "pause");
                        break;
                    }
                    case AUD_PREV: {
                        i.putExtra("command", "previous");
                        break;
                    }
                    case AUD_STOP: {
                        i.putExtra("command", "stop");
                        break;
                    }
                }
                if (i.getStringExtra("command") != null)
                    context.getApplicationContext().sendBroadcast(i);

            }
        }

        public static int stopVideo(Context context) {
            MediaController controller = new MediaController(context);
            controller.setVisibility(View.GONE);
            controller.setMediaPlayer(null);
            return VID_STOPPED;
        }
    }

    public static class BlueTooth {
        public static final int ENABLED = 0;
        public static final int CONNECTED = 0;
        public static final int ALREADY_ENABLED = 1;
        public static final int DISABLED = 2;
        public static final int DISCONNECTED = 2;
        public static final int ALREADY_DISABLED = 3;
        public static final int ERROR = -1;
        public static final int BLUETOOTH_CONNECTION_ERROR = -2;

        @SuppressLint("MissingPermission")
        public static int set(boolean choice) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            boolean isEnabled = bluetoothAdapter.isEnabled();
            if (isEnabled && choice) {
                return ALREADY_ENABLED;
            }
            if (choice) {
                if (bluetoothAdapter.enable())
                    return ENABLED;
                else
                    return ERROR;
            }
            if (!isEnabled) {
                return ALREADY_DISABLED;
            }
            if (bluetoothAdapter.disable())
                return DISABLED;
            else
                return ERROR;
        }

        public static boolean isConnected(Context context) {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
            @SuppressLint("MissingPermission")
            List<BluetoothDevice> connected = manager.getConnectedDevices(BluetoothProfile.GATT_SERVER);
            return connected.size() > 0;
        }

        public static int connected(Context context) {
            if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                if (isConnected(context)) {
                    return CONNECTED;
                } else {
                    return BLUETOOTH_CONNECTION_ERROR;//"No Device is Connected with Bluetooth.";
                }
            } else {
                return DISCONNECTED;//"Bluetooth is Not Enabled."
            }
        }

        public static void connection(Context context) {
            set(true);
            context.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public static class WIFI {
        public static String set(Context context, boolean choice) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            boolean isEnabled = wifiManager.isWifiEnabled();
            if (isEnabled && choice) {
                return "Already Enabled.";
            }
            if (choice) {
                if (wifiManager.setWifiEnabled(true))
                    return "Enabled.";
                else
                    return "Unable to Enable.";
            }
            if (!isEnabled) {
                return "Already Disabled.";
            }
            if (wifiManager.setWifiEnabled(false))
                return "Disabled.";
            else
                return "Unable to disable.";
        }

        public static boolean isConnected(Context context) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return wifiManager.isWifiEnabled();
        }
    }

    public static class Send {
        public static void share(Context context, String path) {
            if (path == null) return;
            try {
                Uri uri = FileProvider.getUriForFile(context, "com.wasitech.basics.FileProvider", new File(path));
                if (uri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                    shareIntent.setDataAndType(uri, context.getContentResolver().getType(uri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                }
            } catch (Exception e) {
                Issue.print(e, Send.class.getName());
            }
        }
    }


    public static WindowManager.LayoutParams WindowServicesParams(int posX, int posY, int gravitySide) {
        WindowManager.LayoutParams params;
        {
            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | gravitySide;
            params.x = posX;
            params.y = posY;
        }
        return params;
    }


    public static String FlashLight(Context context, boolean enable) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return "As far I know, Your phone don't have any.";
        }
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        boolean done;
        try {
            manager.setTorchMode((manager.getCameraIdList())[0], enable);
            done = true;
        } catch (CameraAccessException e) {
            done = false;
            Issue.print(e, Utils.class.getName());
        }
        if (done) {
            if (enable)
                return "FlashLight On.";
            else
                return "FlashLight Off.";
        }
        return "Sorry, Something went wrong.";
    }


    public static String LockPhone(Context context, ComponentName cm, boolean lock) {
        DevicePolicyManager deviceManger = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (lock) {
            if (deviceManger.isAdminActive(cm)) {
                deviceManger.lockNow();
                return "Phone Locked.";
            } else {
                return "Error Found.";
            }
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
            wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        }
        return "";
    }

    public static boolean isNLServiceRunning(Context context) {
        return NotificationManagerCompat.getEnabledListenerPackages(context.getApplicationContext()).contains(context.getPackageName());
    }

    public static boolean isMyServiceRunning(Context c, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int percent(int tot, int cur) {
        int p = cur * 100;
        return p / tot;
    }

    public static void Log(String text) {
        Log.i(TAG, text);
    }

    /**
     * @return min 2 two char
     */

    public static String Min2(Object o) {
        if (o.toString().length() == 1)
            return "0" + o;
        else
            return o.toString();
    }

    /**
     * @implNote Preference is SnackBar but if not possible Toast will pop and on error Log will generate
     */
    public static void NotifyToUser(View view, String msg) {
        try {
            Snackbar.make(view.findViewById(R.id.content), msg, Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            try {
                toasting(view.getContext(), msg);
            } catch (Exception e1) {
                Issue.Log(e1, "NotifyToUser(String msg): " + msg);
            }
        }
    }

    /**
     * @return Arraylist without ny duplicate
     */

    public static ArrayList<String> removeDuplicate(ArrayList<String> list) {
        Set<String> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }

    public static void toasting(Context context, String text) {
        try {
            Toast toast1 = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast1.setGravity(80, 0, 500);
            toast1.show();
        } catch (Exception e) {
            Issue.print(e, Utils.class.getName());
        }
    }
}
