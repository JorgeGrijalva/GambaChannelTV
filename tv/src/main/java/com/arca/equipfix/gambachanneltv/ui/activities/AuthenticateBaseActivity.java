package com.arca.equipfix.gambachanneltv.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.data.network.model.enums.RegistrationStatus;
import com.arca.equipfix.gambachanneltv.ui.local_components.CustomDialog;

import java.util.Calendar;


import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.FIVE_MINUTES;

import static com.arca.equipfix.gambachanneltv.utils.AppConstants.REGISTRATION_STATUS_CHECK_TIME_WORKING_ACTIVITIES;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.TWENTY_MINUTES;


/**
 * Created by gabri on 7/4/2018.
 */

public abstract class AuthenticateBaseActivity extends BaseActivity {


    protected CustomDialog reminderDialog;

    protected  Handler remindersHandler ;
    protected  Runnable  getRemindersRunnable = () -> {
        getReminders();
      //  checkReminders();
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        registrationHandler = new Handler();
        registrationHandler.postDelayed(registrationStatus, REGISTRATION_STATUS_CHECK_TIME_WORKING_ACTIVITIES);

        remindersHandler = new Handler();

        getReminders();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationHandler = new Handler();
        registrationHandler.postDelayed(registrationStatus, REGISTRATION_STATUS_CHECK_TIME_WORKING_ACTIVITIES);

        remindersHandler = new Handler();

        getReminders();

    }

    @Override
    public void onRegistration() {
        if(registrationResponse != null && registrationResponse.getStatus()== RegistrationStatus.ACTIVE)
        {
            Profile profile = dataManager.getSelectedProfile();
            if(profile != null) {
                callStartSession(profile);
            }
        }
        else
        {

            openActivityOnTokenExpire();
        }

    }

    @Override
    public void onSessionStarted(SessionInformation sessionInformation) {
        if(sessionInformation  != null &&  sessionInformation.getAccessToken() != null && !sessionInformation.getAccessToken().equals(EMPTY_STRING))
        {
            registrationHandler.removeCallbacks(registrationStatus);
            registrationHandler.postDelayed(registrationStatus, REGISTRATION_STATUS_CHECK_TIME_WORKING_ACTIVITIES);
        }
        else
        {
            openActivityOnTokenExpire();
        }

    }

    @Override
    protected void onPause() {
        if(registrationHandler != null)
        {
            registrationHandler.removeCallbacks(registrationStatus);

        }

        if(remindersHandler != null)
        {
            remindersHandler.removeCallbacks(getRemindersRunnable);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(registrationHandler != null)
        {
            registrationHandler.removeCallbacks(registrationStatus);
            //Ask for new token right away
            registrationHandler.post(registrationStatus);
        }
        if(remindersHandler == null)
        {
            remindersHandler = new Handler();
        }
        if(remindersHandler != null)
        {
            remindersHandler.postDelayed(getRemindersRunnable, FIVE_MINUTES);
        }
        super.onResume();
    }

    @Override
    public void onSessionError() {
        openActivityOnTokenExpire();

    }

    protected void getReminders()
    {
        if(dataManager != null) {
            AuthenticateBaseActivity.this.reminders = dataManager.getReminders();
            remindersHandler.removeCallbacks(getRemindersRunnable);
            remindersHandler.postDelayed(getRemindersRunnable, TWENTY_MINUTES);
            checkReminders();
         /*   dataManager.getReminders()
                    .onErrorReturn(error ->
                    {
                        return new ArrayList<>();
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reminders -> {
                        AuthenticateBaseActivity.this.reminders = reminders;
                        remindersHandler.removeCallbacks(getRemindersRunnable);
                        remindersHandler.postDelayed(getRemindersRunnable, TWENTY_MINUTES);
                        checkReminders();
                    });*/
        }
    }

    private  void checkReminders()
    {
        if(reminderDialog != null && reminderDialog.isShowing())
        {
            reminderDialog.hide();
        }
        if(reminders!= null && reminders.size()>0)
        {
            Calendar currentDate = Calendar.getInstance();
            for (ChannelProgram reminder: reminders) {
                if(reminder.getStartDate().getTime() - currentDate.getTime().getTime() < FIVE_MINUTES)
                {

                    if(!dataManager.reminderShowed(reminder.getChannelId(), reminder.getId().intValue(), reminder.getStartDate().toString())) {

                        boolean showReminder = true;
                        if (this instanceof LivePlayerActivity) {
                            LivePlayerActivity activity = (LivePlayerActivity) this;
                            showReminder = activity.channels.get(activity.currentRow).getId() != reminder.getChannelId();
                        }
                        if (showReminder) {

                            long diffMS = reminder.getStartDate().getTime() - currentDate.getTime().getTime();
                            long minutes = diffMS / 1000 / 60;
                            String message = String.format(getString(R.string.program_will_start_soon), reminder.getTitle(), minutes, reminder.getChannelName());

                            reminderDialog = new CustomDialog(this, true, getString(R.string.dont_miss_it), message, getString(R.string.ok), EMPTY_STRING, true);
                            dataManager.setReminderShowed(reminder.getChannelId(), reminder.getId().intValue(), reminder.getStartDate().toString());
                        }
                    }


                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(reminderDialog != null)
        {
            reminderDialog.hide();
            reminderDialog = null;
        }

        if(remindersHandler != null)
        {
            remindersHandler.removeCallbacks(getRemindersRunnable);
            remindersHandler = null;
        }
    }

    @Override
    protected void onDestroy() {

        if(reminderDialog != null)
        {
            reminderDialog.hide();
            reminderDialog = null;
        }

        if(remindersHandler != null)
        {
            remindersHandler.removeCallbacks(getRemindersRunnable);
            remindersHandler = null;
        }

        super.onDestroy();
    }
}
