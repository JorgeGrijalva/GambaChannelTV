package com.arca.equipfix.gambachanneltv.ui.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.arca.equipfix.gambachanneltv.BuildConfig;
import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.Card;
import com.arca.equipfix.gambachanneltv.data.network.model.LastVersionInformation;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.network.model.RegistrationResponse;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.data.prefs.model.LoginType;
import com.arca.equipfix.gambachanneltv.ui.adapters.CardsAdapter;
import com.arca.equipfix.gambachanneltv.ui.local_components.CustomDialog;
import com.arca.equipfix.gambachanneltv.ui.local_components.InputDialog;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.CURRENT_PROFILE_EXTRA;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.REGISTRATION_INFORMATION;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.UNINSTALL_OLD_VERSION;

public class SplashActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @BindView(R.id.statusText)
    TextView statusText;
    @BindView(R.id.retryText)
    TextView retryText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));



        Configuration configuration = getResources().getConfiguration();
        Locale locale = dataManager.getPreferredLanguage() == "ES" ? new Locale("es", "MX") : Locale.US;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){

            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            getResources().updateConfiguration(configuration,displayMetrics);
        }

        boolean result =  checkOldVersions();
        if(result)
        {
            checkLastVersion();
        }
    }

    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            return true;
        }
    }



    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages)
    {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packs.size();i++)
        {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            if(!p.packageName.contains("gambachanneltv"))
            {
                continue;
            }
            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            res.add(newInfo);
        }
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UNINSTALL_OLD_VERSION)
        {
            checkLastVersion();
        }
    }

    private boolean checkOldVersions()
    {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            ArrayList<PInfo> installedApps = getInstalledApps(false);
            for (PInfo oldGamba: installedApps) {
                if(oldGamba.versionCode < pInfo.versionCode)
                {
                    statusText.setText(R.string.uninstall_old_version);
                    Uri packageUri = Uri.parse("package:"+ oldGamba.pname);
                    Intent uninstallIntent = new Intent(Intent.ACTION_DELETE , packageUri);
                    startActivityForResult(uninstallIntent, UNINSTALL_OLD_VERSION);
                    return  false;
                }

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  true;
    }

    private void checkLastVersion()
    {
        if(!isNetworkConnected()) {
            statusText.setText(getString(R.string.no_network_available));
            retryText.setVisibility(View.VISIBLE);
            return;
        }

        haveStoragePermission();
        dataManager.getLastVersion()
                .onErrorReturn(error ->
                {
                    return new LastVersionInformation();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lastVersion ->
                {
                    if(lastVersion!= null && !lastVersion.equals(EMPTY_STRING))
                    {
                        String[] versionParts= lastVersion.getLastVersion().split("\\.");
                        try {
                            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                            statusText.setText(R.string.checking_last_version);
                            String currentVersion = pInfo.versionName;
                            String[] currentVersionParts= currentVersion.split("\\.");
                            Integer lastVersionValue =  Integer.parseInt(versionParts[0])*10000 + Integer.parseInt(versionParts[1])*100 + Integer.parseInt(versionParts[2]);
                            Integer currentVersionValue =  Integer.parseInt(currentVersionParts[0])*10000 + Integer.parseInt(currentVersionParts[1])*100 + Integer.parseInt(currentVersionParts[2]);
                            if( (lastVersionValue > currentVersionValue))
                            {
                                //get destination to update file and set Uri
                                //TODO: First I wanted to store my update .apk file on internal storage for my app but apparently android does not allow you to open and install
                                //aplication with existing package from there. So for me, alternative solution is Download directory in external storage. If there is better
                                //solution, please inform us in comment

                                statusText.setText(R.string.downloading_latest_version);
                                String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                                String fileName = "Gamba.apk";
                                destination += fileName;
                                final Uri uri = Uri.parse("file://" + destination);

                                //Delete update file if exists
                                File file = new File(destination);
                                if (file.exists())
                                    //file.delete() - test this, I think sometimes it doesnt work
                                    file.delete();

                                //get url of app on server
                                String url = lastVersion.getUrl();

                                //set downloadmanager
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                request.setDescription(getString(R.string.downloading_new_version));
                                request.setTitle(getString(R.string.app_name));

                                //set destination
                                request.setDestinationUri(uri);

                                // get download service and enqueue file
                                final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                final long downloadId = manager.enqueue(request);

                                //set BroadcastReceiver to install app when .apk is downloaded
                                BroadcastReceiver onComplete = new BroadcastReceiver() {
                                    public void onReceive(Context ctxt, Intent intent) {
                                        if(statusText == null)
                                        {
                                            return;

                                        }


                                        statusText.setText(R.string.install_last_version);

                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                        {
                                            Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                                            install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            Uri apkUri = FileProvider.getUriForFile(SplashActivity.this,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    file);
                                            install.setData( apkUri );
                                            startActivity(install);

                                        }
                                        else
                                        {
                                            Uri apkUri = Uri.fromFile(file);
                                            Intent installOld = new Intent(Intent.ACTION_VIEW);
                                            installOld.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                            installOld.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(installOld);

                                        }

                                        unregisterReceiver(this);
                                        finish();
                                    }
                                };
                                //register receiver for when .apk download is compete
                                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                            }
                            else
                            {
                                validateRegistration();
                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        validateRegistration();
                    }
                });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode =  event.getKeyCode();
        boolean returnValue = false;
        if(action == ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KEYCODE_DPAD_CENTER :
                case KEYCODE_ENTER :
                    returnValue = true;
                    validateRegistration();
                    break;
            }
            if(returnValue) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }



    void validateRegistration()
    {
        statusText.setText(getString(R.string.connecting_to_server));
        retryText.setVisibility(View.GONE);

        if(!isNetworkConnected()) {
            statusText.setText(getString(R.string.no_network_available));
            retryText.setVisibility(View.VISIBLE);
            return;
        }

    //    List<Profile> profiles = dataManager.getLocalProfileList();

        callRegistrationStatus(true);
    }


    void decideNextActivity(RegistrationResponse registrationResponse)
    {
        Intent intent = null;
        switch (registrationResponse.getStatus())
        {
            case ERROR:
                statusText.setText(getString(R.string.server_connection_error));
                return;
            case NEW:
                dataManager.setLoginType(LoginType.DEMO);
                intent = StartActivity.getStartIntent(this);
                intent.putExtra(REGISTRATION_INFORMATION,  registrationResponse);
                break;
            case ACTIVE:
                dataManager.setLoginType(LoginType.CLIENT);
                intent = MainMenuActivity.getStartIntent(this);
                intent.putExtra(CURRENT_PROFILE_EXTRA, currentProfile );
                break;
            case EXPIRED:
                dataManager.setLoginType(LoginType.DEMO);
                intent = StartActivity.getStartIntent(this);
                intent.putExtra(REGISTRATION_INFORMATION,  registrationResponse);
                break;
            case BLOCKED:
                break;
        }
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private  void askPassword(Profile profile)
    {
        final InputDialog codeDialog = new InputDialog(this, true, getString(R.string.password_protected), EMPTY_STRING, getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        codeDialog.setOnLeftButtonClick(view ->
                dataManager.validateProfilePassword((int)profile.getId(), codeDialog.getInput())
                .onErrorReturn(error->
                {
                    return false;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valid -> {
                    if(valid)
                    {
                        callStartSession(profile);
                    }
                    else
                    {
                        CustomDialog badPinDialog= new CustomDialog(SplashActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                        badPinDialog.setOnLeftButtonClick(leftButton->
                        {
                            askPassword(profile);
                        });
                    }
                }));

    }


    @Override
    public void onRegistration() {
        statusText.setText(getString(R.string.resolving_status));
        if(registrationResponse == null)
        {
            statusText.setText(getString(R.string.cant_create_session));
            return;
        }

        List<Profile> profiles = null;
        if(registrationResponse.getProfiles()!= null && registrationResponse.getProfiles().length>0)
        {
            profiles = Arrays.asList(registrationResponse.getProfiles());
            dataManager.saveProfileList(profiles);
        }
        else
        {
            profiles = dataManager.getLocalProfileList();
        }
        selectStartProfile(profiles);
    }

    void selectStartProfile(List<Profile> profiles)
    {
        if(profiles.size() == 0)
        {
            statusText.setText(getString(R.string.cant_create_session));
            return;
        }

        if(profiles.size() == 1)
        {
            statusText.setText(R.string.creating_session);
            callStartSession(profiles.get(0));
        }
        else
        {
            ArrayList<Card> profileCards = new ArrayList<>();
            for (Profile profile: profiles) {
                profileCards.add(new Card((int)profile.getId(), profile.getName(),  Card.Type.DEFAULT));
            }

            CardsAdapter adapter = new CardsAdapter(SplashActivity.this, 0, profileCards);
            new LovelyChoiceDialog(this)
                    .setTopColorRes(R.color.gambaRed)
                    .setTitle(R.string.select_profile)
                    .setMessage(getString(R.string.profiles))
                    .setItems(adapter, (position, item) -> {
                        Profile profile = profiles.get(position);
                        if(profile != null) {
                            if(profile.getPasswordProtected())
                            {
                                askPassword(profile);
                            }
                            else {
                                callStartSession(profile);
                            }
                        }
                        else
                        {
                            finish();
                        }
                    })
                    .show();
        }
    }



    @Override
    public void onSessionStarted(SessionInformation sessionInformation) {
        statusText.setText(R.string.all_set);
        decideNextActivity(registrationResponse);
    }

    @Override
    public void onSessionError() {
        statusText.setText(R.string.cant_store_session);
    }

    class PInfo {
        private String appname = "";
        private String pname = "";
        private String versionName = "";
        private int versionCode = 0;
    }
}
