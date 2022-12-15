package com.arca.equipfix.gambachanneltv.data.db;

import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.models.Profile;

import java.util.Date;
import java.util.List;

/**
 * Created by gabri on 6/14/2018.
 */

public interface DbHelper {

    boolean insertChannelProgram(ChannelProgram channelProgram);
    List<ChannelProgram> getProgramsByChannel(int channelId, Date afterNow);
    Date getLastDateChannel(int channelId);

    boolean addProgramReminder(int channelId, String title, boolean unique);
    boolean deleteProgramReminder(int channelId, String title);
    List<ChannelProgram> getReminders();

    List<Profile> getLocalProfileList();
    boolean saveProfileList(List<Profile> profiles);
    Profile getSelectedProfile();
    boolean setSelectedProfile(long id);


}
