package com.arca.equipfix.gambachanneltv.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.network.model.LinkedDeviceInfo;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;
import com.arca.equipfix.gambachanneltv.ui.adapters.ProfileAdapter;
import com.arca.equipfix.gambachanneltv.ui.local_components.CustomDialog;
import com.arca.equipfix.gambachanneltv.ui.local_components.InputDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static com.arca.equipfix.gambachanneltv.utils.AppConstants.EMPTY_STRING;

public class ProfilesActivity extends BaseActivity implements View.OnClickListener {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProfilesActivity.class);
    }

    @BindView(R.id.profliesListBox) ListView profilesListBox;

    List<Profile> profiles;
    ProfileAdapter profilesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        setUnBinder(ButterKnife.bind(this));

        dataManager.getProfileList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error->
                        {
                            return new ArrayList<>();
                        }
                )
                .subscribe(profiles -> {
                    ProfilesActivity.this.profiles = profiles;
                    profiles.add(0, new Profile(-1, EMPTY_STRING, getString(R.string.add_new_profile), false, false));
                    profiles.add(1, new Profile(-2, EMPTY_STRING, getString(R.string.change_adults_password), false, false));
                    profiles.add(2, new Profile(-3, EMPTY_STRING, getString(R.string.link_device), false, false));
                    profilesAdapter = new ProfileAdapter(new ArrayList<>(profiles), ProfilesActivity.this);
                    profilesListBox.setAdapter(profilesAdapter);
                    profilesListBox.requestFocus();
                });

        profilesListBox.setOnFocusChangeListener((view, focused) -> {
            if(focused)
            {
                profilesListBox.setSelector(R.color.selectedItem);
            }
            else
            {
                profilesListBox.setSelector(R.color.transparent);
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        boolean result = false;
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (event.getKeyCode())
            {
                case KEYCODE_DPAD_CENTER:
                case KEYCODE_ENTER:
                    switch (profilesListBox.getSelectedItemPosition())
                    {
                        case 0:
                            final InputDialog requirePasswordDialog = new InputDialog(this, true, getString(R.string.require_access_code), EMPTY_STRING, getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            requirePasswordDialog.setOnLeftButtonClick(view -> dataManager.validateAccessCode(requirePasswordDialog.getInput())
                                    .onErrorReturn(error->
                                    {
                                        return false;
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(valid -> {
                                        if(valid)
                                        {
                                            addProfile();
                                        }
                                        else
                                        {
                                            new CustomDialog(ProfilesActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                                        }
                                    }));

                            break;
                        case 1:
                            final InputDialog changePasswordDialog = new InputDialog(this, true, getString(R.string.require_access_code), EMPTY_STRING, getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            changePasswordDialog.setOnLeftButtonClick(view ->
                                    dataManager.validateAccessCode(changePasswordDialog.getInput())
                                    .onErrorReturn(error->
                                    {
                                        return false;
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(valid -> {
                                        if(valid)
                                        {
                                            changeAdultsPassword();
                                        }
                                        else
                                        {
                                            new CustomDialog(ProfilesActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                                        }
                                    }));

                            break;
                        case 2:
                            dataManager.getLinkedDeviceInfo()
                                    .onErrorReturn(throwable ->
                                            new LinkedDeviceInfo())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(linkedDeviceInfo -> {
                                       if(linkedDeviceInfo != null)
                                       {
                                           if(linkedDeviceInfo.getSerialNumber() != null && linkedDeviceInfo.getSerialNumber().length()>0)
                                           {
                                               CustomDialog deleteDevice =  new CustomDialog(ProfilesActivity.this, true, getString(R.string.one_device_linked), getString(R.string.want_to_delete_linked), getString(R.string.yes), getString(R.string.no), true );
                                               deleteDevice.setOnLeftButtonClick(view ->
                                                       dataManager.deleteLinkedDevice()
                                                       .subscribeOn(Schedulers.io())
                                                       .observeOn(AndroidSchedulers.mainThread())
                                                       .onErrorReturn(throwable ->
                                                       {
                                                           return false;
                                                       })
                                                       .subscribe(deleted -> {
                                                           if(deleted)
                                                           {
                                                               new CustomDialog(ProfilesActivity.this, true, getString(R.string.delete_success),getString(R.string.linked_deleted), getString(R.string.ok), EMPTY_STRING, true );
                                                           }
                                                           else
                                                           {
                                                               new CustomDialog(ProfilesActivity.this, true, getString(R.string.error_deleting), getString(R.string.cannot_delete_linked), getString(R.string.ok), EMPTY_STRING, true );
                                                           }
                                                       }));
                                           }
                                           else
                                           {
                                               new CustomDialog(ProfilesActivity.this, true, getString(R.string.your_linking_code), String.format(getString(R.string.linking_code_format_message), linkedDeviceInfo.getRegistrationCode()), getString(R.string.ok), EMPTY_STRING, true );
                                           }
                                       }
                                    });


                            break;
                        default:
                            Profile profile = profiles.get(profilesListBox.getSelectedItemPosition());
                            if(profile.getPasswordProtected())
                            {
                                final InputDialog codeDialog = new InputDialog(this, true, getString(R.string.password_protected), EMPTY_STRING, getString(R.string.send_code), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                                codeDialog.setOnLeftButtonClick(view -> dataManager.validateProfilePassword((int)profile.getId(), codeDialog.getInput())
                                        .onErrorReturn(error->
                                        {
                                            return false;
                                        })
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(valid -> {
                                            if(valid)
                                            {
                                                editProfile(profile);
                                            }
                                            else
                                            {
                                                new CustomDialog(ProfilesActivity.this, true, getString(R.string.invalid_access_code_title), getString(R.string.invalid_access_code), getString(R.string.ok), EMPTY_STRING, true );
                                            }
                                        }));
                            }
                            else
                            {
                                editProfile(profile);
                            }
                            result = true;
                            break;
                    }
                    break;
            }
            if(result)
            {
                return true;
            }
        }


        return super.dispatchKeyEvent(event);
    }

    private void addProfile()
    {
        InputDialog nameDialog = new InputDialog(this, true, getString(R.string.profile_name), EMPTY_STRING, getString(R.string.ok), true, InputType.TYPE_CLASS_TEXT);
        nameDialog.setOnLeftButtonClick(view -> {
            if(nameDialog.getInput() != null && nameDialog.getInput().length()>0)
            {
                CustomDialog enableAdultsDialog = new CustomDialog(this, true, getString(R.string.enable_adults) , getString(R.string.wanto_to_enable_adults), getString(R.string.yes),getString(R.string.no), true);
                enableAdultsDialog.setOnLeftButtonClick(leftButton->
                {
                    CustomDialog passwordProtectDialog = new CustomDialog(this, true, getString(R.string.password_protect), getString(R.string.want_to_protect_profile), getString(R.string.yes),getString(R.string.no), true);
                    passwordProtectDialog.setOnLeftButtonClick(protectLeftButton->
                    {
                        InputDialog profilePasswordDialog = new InputDialog(this, true, getString(R.string.profile_password),EMPTY_STRING, getString(R.string.save), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        profilePasswordDialog.setOnLeftButtonClick(profilePasswordLeftButton->
                        {
                            try {
                                Integer.parseInt(profilePasswordDialog.getInput());
                                callCreateProfile(new Profile(nameDialog.getInput(), true, true, profilePasswordDialog.getInput()));

                            } catch (NumberFormatException e) {
                                new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
                            }

                        });
                    });
                    passwordProtectDialog.setOnRightButtonClick(protectRightButton->
                    {
                        callCreateProfile(new Profile(nameDialog.getInput(), false, true, EMPTY_STRING));
                    });
                });
                enableAdultsDialog.setOnRightButtonClick(rightButton->
                {
                    CustomDialog confirmDisableAdultsDialog = new CustomDialog(this, true, getString(R.string.confirmation), getString(R.string.adults_cannot_be_enabled_later), getString(R.string.yes),getString(R.string.no), true);
                    confirmDisableAdultsDialog.setOnRightButtonClick(confirmLeftButon->
                    {
                        CustomDialog passwordProtectDialog = new CustomDialog(this, true, getString(R.string.password_protect),  getString(R.string.want_to_protect_profile), getString(R.string.yes),getString(R.string.no), true);
                        passwordProtectDialog.setOnLeftButtonClick(protectLeftButton->
                        {
                            InputDialog profilePasswordDialog = new InputDialog(this, true, getString(R.string.profile_password), EMPTY_STRING, getString(R.string.save), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            profilePasswordDialog.setOnLeftButtonClick(profilePasswordLeftButton->
                            {
                                try {
                                    Integer.parseInt(profilePasswordDialog.getInput());
                                    callCreateProfile(new Profile(nameDialog.getInput(), true, true, profilePasswordDialog.getInput()));

                                } catch (NumberFormatException e) {
                                    new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
                                }
                            });
                        });
                        passwordProtectDialog.setOnRightButtonClick(protectRightButton->
                        {
                            callCreateProfile(new Profile(nameDialog.getInput(), false, true, EMPTY_STRING));
                        });

                    });

                    confirmDisableAdultsDialog.setOnLeftButtonClick(confirmDisableLeftButtoin->
                    {
                        CustomDialog passwordProtectDialog = new CustomDialog(this, true,  getString(R.string.password_protect),  getString(R.string.want_to_protect_profile), getString(R.string.yes),getString(R.string.no), true);
                        passwordProtectDialog.setOnLeftButtonClick(protectLeftButton->
                        {
                            InputDialog profilePasswordDialog = new InputDialog(this, true,  getString(R.string.profile_password),EMPTY_STRING, getString(R.string.save), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            profilePasswordDialog.setOnLeftButtonClick(profilePasswordLeftButton->
                            {
                                try {
                                    Integer.parseInt(profilePasswordDialog.getInput());
                                    callCreateProfile(new Profile(nameDialog.getInput(), true, false, profilePasswordDialog.getInput()));

                                } catch (NumberFormatException e) {
                                    new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
                                }
                            });
                        });
                        passwordProtectDialog.setOnRightButtonClick(protectRightButton->
                        {
                            callCreateProfile(new Profile(nameDialog.getInput(), false, false, EMPTY_STRING));
                        });
                    });
                });

            }
            else
            {
                new CustomDialog(this, true, getString(R.string.name_empty), getString(R.string.profile_name_not_empty), getString(R.string.ok), EMPTY_STRING, true);
            }
        });
    }

    private void callCreateProfile(Profile profile)
    {
        dataManager.createProfile(profile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error->
                {
                    return 0;
                })
                .subscribe(profileId -> {
                    if(profileId>0)
                    {
                        profiles.add(profile);
                        profile.setId(profileId);
                        profilesAdapter.addItem(profile);
                    }
                });
    }

    private void editProfile(Profile profile)
    {
        InputDialog nameDialog = new InputDialog(this, true, getString(R.string.profile_name), profile.getName(), getString(R.string.ok), true, InputType.TYPE_CLASS_TEXT);
        nameDialog.setOnLeftButtonClick(view -> {
            if(nameDialog.getInput() != null && nameDialog.getInput().length()>0)
            {
                profile.setName(nameDialog.getInput());

            }
            if(profile.getEnableAdults()) {
                CustomDialog enableAdultsDialog = new CustomDialog(this, true, getString(R.string.enable_adults) , getString(R.string.wanto_to_enable_adults), getString(R.string.yes),getString(R.string.no), true);
                enableAdultsDialog.setOnLeftButtonClick(leftButton->
                {
                    CustomDialog passwordProtectDialog = new CustomDialog(this, true, getString(R.string.password_protect), getString(R.string.want_to_protect_profile), getString(R.string.yes),getString(R.string.no), true);
                    passwordProtectDialog.setOnLeftButtonClick(protectLeftButton->
                    {
                        InputDialog profilePasswordDialog = new InputDialog(this, true, getString(R.string.change_profile_password), EMPTY_STRING, getString(R.string.save), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        profilePasswordDialog.setOnLeftButtonClick(profilePasswordLeftButton->
                        {
                            try {
                                Integer.parseInt(profilePasswordDialog.getInput());
                                profile.setPasswordProtected(true);
                                profile.setProfilePassword(profilePasswordDialog.getInput());
                                callEditProfile(profile);

                            } catch (NumberFormatException e) {
                                new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
                            }

                        });
                    });
                    passwordProtectDialog.setOnRightButtonClick(protectRightButton->
                    {
                        profile.setPasswordProtected(false);
                        callEditProfile(profile);
                    });
                });
                enableAdultsDialog.setOnRightButtonClick(rightButton ->
                {
                    CustomDialog confirmDisableAdultsDialog = new CustomDialog(this, true, getString(R.string.confirmation), getString(R.string.adults_cannot_be_enabled_later), getString(R.string.yes),getString(R.string.no), true);
                    confirmDisableAdultsDialog.setOnRightButtonClick(confirmLeftButon->
                    {
                        CustomDialog passwordProtectDialog = new CustomDialog(this, true, getString(R.string.password_protect),  getString(R.string.want_to_protect_profile), getString(R.string.yes),getString(R.string.no), true);
                        passwordProtectDialog.setOnLeftButtonClick(protectLeftButton->
                        {
                            InputDialog profilePasswordDialog = new InputDialog(this, true, getString(R.string.profile_password), EMPTY_STRING, getString(R.string.save), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            profilePasswordDialog.setOnLeftButtonClick(profilePasswordLeftButton->
                            {
                                try {
                                    Integer.parseInt(profilePasswordDialog.getInput());
                                    profile.setPasswordProtected(true);
                                    profile.setProfilePassword(profilePasswordDialog.getInput());
                                    callEditProfile(profile);

                                } catch (NumberFormatException e) {
                                    new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
                                }

                            });
                        });
                        passwordProtectDialog.setOnRightButtonClick(protectRightButton->
                        {
                            profile.setPasswordProtected(false);
                            callEditProfile(profile);
                        });

                    });

                    confirmDisableAdultsDialog.setOnLeftButtonClick(confirmDisableLeftButtoin->
                    {
                        CustomDialog passwordProtectDialog = new CustomDialog(this, true,  getString(R.string.password_protect),  getString(R.string.want_to_protect_profile), getString(R.string.yes),getString(R.string.no), true);
                        passwordProtectDialog.setOnLeftButtonClick(protectLeftButton->
                        {
                            InputDialog profilePasswordDialog = new InputDialog(this, true,  getString(R.string.profile_password),EMPTY_STRING, getString(R.string.save), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                            profilePasswordDialog.setOnLeftButtonClick(profilePasswordLeftButton->
                            {
                                try {
                                    Integer.parseInt(profilePasswordDialog.getInput());
                                    profile.setEnableAdults(false);
                                    profile.setPasswordProtected(true);
                                    profile.setProfilePassword(profilePasswordDialog.getInput());
                                    callEditProfile(profile);

                                } catch (NumberFormatException e) {
                                    new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
                                }

                            });
                        });
                        passwordProtectDialog.setOnRightButtonClick(protectRightButton->
                        {
                            profile.setEnableAdults(false);
                            profile.setPasswordProtected(false);
                            callEditProfile(profile);
                        });
                    });
                });

            }
            else
            {
                CustomDialog passwordProtectDialog = new CustomDialog(this, true,  getString(R.string.password_protect),  getString(R.string.want_to_protect_profile), getString(R.string.yes),getString(R.string.no), true);
                passwordProtectDialog.setOnLeftButtonClick(protectLeftButton->
                {
                    InputDialog profilePasswordDialog = new InputDialog(this, true,  getString(R.string.change_profile_password), EMPTY_STRING, getString(R.string.save), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    profilePasswordDialog.setOnLeftButtonClick(profilePasswordLeftButton->
                    {
                        try {
                            Integer.parseInt(profilePasswordDialog.getInput());
                            profile.setPasswordProtected(true);
                            profile.setProfilePassword(profilePasswordDialog.getInput());
                            callEditProfile(profile);

                        } catch (NumberFormatException e) {
                            new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
                        }

                    });
                });
                passwordProtectDialog.setOnRightButtonClick(protectRightButton->
                {
                    profile.setPasswordProtected(false);
                    callEditProfile(profile);
                });


            }
        });

    }

    private void callEditProfile(Profile profile)
    {
        dataManager.updateProfile(profile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error->
                {
                    return false;
                })
                .subscribe(updated -> {
                    if(!updated)
                    {
                        new CustomDialog(ProfilesActivity.this, true, getString(R.string.profile_update_error), getString(R.string.error_updating_profile), getString(R.string.ok), EMPTY_STRING, true );

                    }
                    else
                    {
                        new CustomDialog(ProfilesActivity.this, true,getString(R.string.profile_updated), getString(R.string.profile_saved), getString(R.string.ok), EMPTY_STRING, true );
                    }
                });

    }

    private void changeAdultsPassword()
    {
        InputDialog newPassword = new InputDialog(this, true, getString(R.string.new_adults_password), EMPTY_STRING, getString(R.string.ok), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        try {
            newPassword.setOnLeftButtonClick(view -> {
                int newPassInput = Integer.parseInt(newPassword.getInput());

                InputDialog confirmPassword = new InputDialog(this, true, getString(R.string.confirm_new_password), EMPTY_STRING, getString(R.string.ok), true, InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                confirmPassword.setOnLeftButtonClick(view2 -> {
                    int confirmPassInput = Integer.parseInt(confirmPassword.getInput());
                    if(newPassInput == confirmPassInput) {
                        if(newPassInput < 1000000) {

                            dataManager.updateAccessCode(newPassword.getInput())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .onErrorReturn(error ->
                                    {
                                        return false;
                                    })
                                    .subscribe(updated -> {
                                        if (updated) {
                                            new CustomDialog(ProfilesActivity.this, true, getString(R.string.access_code_updated), getString(R.string.new_access_code_ready), getString(R.string.ok), EMPTY_STRING, true);
                                        } else {
                                            new CustomDialog(ProfilesActivity.this, true, getString(R.string.access_code_update_error), getString(R.string.error_updating_access_code), getString(R.string.ok), EMPTY_STRING, true);
                                        }

                                    });
                        }
                        else
                        {
                            new CustomDialog(ProfilesActivity.this, true, getString(R.string.access_code_update_error), getString(R.string.new_password_too_long), getString(R.string.ok), EMPTY_STRING, true);
                        }
                    }
                    else
                    {
                        new CustomDialog(ProfilesActivity.this, true, getString(R.string.access_code_update_error), getString(R.string.password_doesnt_match), getString(R.string.ok), EMPTY_STRING, true);
                    }

                });

            });


        } catch (NumberFormatException e) {
            new CustomDialog(this, true, getString(R.string.wrong_password_format), getString(R.string.password_only_numbers), getString(R.string.ok), EMPTY_STRING, true);
        }

    }


    @Override
    public void onRegistration() {

    }

    @Override
    public void onSessionStarted(SessionInformation sessionInformation) {

    }

    @Override
    public void onSessionError() {

    }

    @Override
    public void onClick(View view) {

    }
}
