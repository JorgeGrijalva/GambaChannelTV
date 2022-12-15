package com.arca.equipfix.gambachanneltv.data.db;

import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.models.ChannelProgramDao;
import com.arca.equipfix.gambachanneltv.data.models.DaoMaster;
import com.arca.equipfix.gambachanneltv.data.models.DaoSession;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.models.ProfileDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;



@Singleton
public class AppDbHelper implements DbHelper {

    private final DaoSession daoSession ;

    @Inject
    public  AppDbHelper(DbOpenHelper dbOpenHelper)
    {
        daoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }


    @Override
    public boolean insertChannelProgram(ChannelProgram channelProgram) {
        try {
            ChannelProgramDao channelProgramDao = daoSession.getChannelProgramDao();
            QueryBuilder<ChannelProgram> qb = channelProgramDao.queryBuilder();
            qb.where( qb.and(ChannelProgramDao.Properties.ChannelId.eq(channelProgram.getChannelId()),  ChannelProgramDao.Properties.StartDate.eq(channelProgram.getStartDate())));
            if(qb.list().size() ==0) {
                channelProgramDao.insert(channelProgram);
            }
            return true;
        }
        catch (Exception ignore)
        {
            return false;
        }
    }

    @Override
    public List<ChannelProgram> getProgramsByChannel(int channelId, Date afterNow) {
        try {
            ChannelProgramDao channelProgramDao = daoSession.getChannelProgramDao();
            QueryBuilder<ChannelProgram> qb = channelProgramDao.queryBuilder();
            qb.where( qb.and(ChannelProgramDao.Properties.ChannelId.eq(channelId),  ChannelProgramDao.Properties.EndDate.gt(afterNow)));
            return qb.list();

        }
        catch (Exception ignore)
        {
            return null;
        }
    }

    @Override
    public Date getLastDateChannel(int channelId)
    {
        try {
            ChannelProgramDao channelProgramDao = daoSession.getChannelProgramDao();
            QueryBuilder<ChannelProgram> qb = channelProgramDao.queryBuilder();
            qb.where(ChannelProgramDao.Properties.ChannelId.eq(channelId)).orderDesc(ChannelProgramDao.Properties.EndDate).limit(1);
            List<ChannelProgram> lastProgram = qb.list();
            if(lastProgram!= null && lastProgram.size()==1)
            {
                return lastProgram.get(0).getEndDate();
            }

            return new Date();
        }
        catch (Exception ignore)
        {
            return null;
        }
    }



    @Override
    public boolean addProgramReminder(int channelId, String title, boolean unique ) {
        try {

            ChannelProgramDao channelProgramDao = daoSession.getChannelProgramDao();
            QueryBuilder<ChannelProgram> qb = channelProgramDao.queryBuilder();
            qb.where( qb.and(ChannelProgramDao.Properties.ChannelId.eq(channelId),  ChannelProgramDao.Properties.Title.eq(title)));
            List<ChannelProgram> programList = qb.list();
            for (ChannelProgram channelProgram: programList) {
                channelProgram.setReminder(true);
                channelProgramDao.update(channelProgram);
            }
            return true;

        }
        catch (Exception ignore)
        {
            return false;
        }
    }

    @Override
    public boolean deleteProgramReminder(int channelId, String title) {
        try {

            ChannelProgramDao channelProgramDao = daoSession.getChannelProgramDao();
            QueryBuilder<ChannelProgram> qb = channelProgramDao.queryBuilder();
            qb.where( qb.and(ChannelProgramDao.Properties.ChannelId.eq(channelId),  ChannelProgramDao.Properties.Title.eq(title)));
            List<ChannelProgram> programList = qb.list();
            for (ChannelProgram channelProgram: programList) {
                channelProgram.setReminder(false);
                channelProgramDao.update(channelProgram);
            }
            return true;

        }
        catch (Exception ignore)
        {
            return false;
        }
    }

    @Override
    public List<ChannelProgram> getReminders() {
        try {
            ChannelProgramDao channelProgramDao = daoSession.getChannelProgramDao();
            QueryBuilder<ChannelProgram> qb = channelProgramDao.queryBuilder();
            Calendar calendar = Calendar.getInstance();
            qb.where(qb.and(ChannelProgramDao.Properties.Reminder.eq(true), ChannelProgramDao.Properties.StartDate.gt(calendar.getTime())));
            return qb.list();
        }
        catch (Exception ignore)
        {
            return null;
        }
    }

    @Override
    public List<Profile> getLocalProfileList() {
        try {

            ProfileDao profileDao = daoSession.getProfileDao();
            QueryBuilder<Profile> qb = profileDao.queryBuilder();
            return qb.list();
        }
        catch (Exception ignore)
        {
            return null;
        }
    }

    @Override
    public boolean saveProfileList(List<Profile> profiles) {
        try {

            ProfileDao profileDao = daoSession.getProfileDao();
            QueryBuilder<Profile> qb = profileDao.queryBuilder();
            List<Profile> profileList = qb.list();

            for (Profile profile: profileList) {
                profileDao.delete(profile);
            }

            for (Profile profile: profiles) {
                profileDao.insert(profile);
            }
            return true;

        }
        catch (Exception ignore)
        {
            return false;
        }
    }

    @Override
    public Profile getSelectedProfile() {
        try {

            ProfileDao profileDao = daoSession.getProfileDao();
            QueryBuilder<Profile> qb = profileDao.queryBuilder();
            qb.where(ProfileDao.Properties.Selected.eq(true)).limit(1);
            List<Profile> selectedProfiles = qb.list();
            if(selectedProfiles.size() > 0)
            {
                return selectedProfiles.get(0);
            }
            else
            {
                QueryBuilder<Profile> qb2 = profileDao.queryBuilder();
                List<Profile> profiles = qb2.list();
                if(profiles.size() > 0)
                {
                    return profiles.get(0);
                }
                else
                {
                    return null;
                }
            }
        }
        catch (Exception ignore)
        {
            return null;
        }
    }

    @Override
    public boolean setSelectedProfile(long id) {
        try {

            ProfileDao profileDao = daoSession.getProfileDao();
            QueryBuilder<Profile> qb = profileDao.queryBuilder();
            qb.where(ProfileDao.Properties.Selected.eq(true)).limit(1);
            List<Profile> selectedProfiles = qb.list();
            if(selectedProfiles.size() > 0)
            {
                Profile selected =  selectedProfiles.get(0);
                if(selected.getId() != id)
                {
                    selected.setSelected(false);
                    profileDao.update(selected);
                }
                else
                {
                    return true;
                }
            }

            QueryBuilder<Profile> qb2 = profileDao.queryBuilder();
            qb2.where(ProfileDao.Properties.Id.eq(id)).limit(1);
            List<Profile> newSelected = qb2.list();
            if(newSelected.size()>0)
            {
                Profile toUpdate = newSelected.get(0);
                toUpdate.setSelected(true);
                profileDao.update(toUpdate);
            }
            return true;
        }
        catch (Exception ignore)
        {
            return false;
        }
    }
}
