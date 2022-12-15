package com.arca.equipfix.gambachanneltv.data.models;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.arca.equipfix.gambachanneltv.data.models.ChannelProgram;
import com.arca.equipfix.gambachanneltv.data.models.Profile;
import com.arca.equipfix.gambachanneltv.data.models.Reminder;

import com.arca.equipfix.gambachanneltv.data.models.ChannelProgramDao;
import com.arca.equipfix.gambachanneltv.data.models.ProfileDao;
import com.arca.equipfix.gambachanneltv.data.models.ReminderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig channelProgramDaoConfig;
    private final DaoConfig profileDaoConfig;
    private final DaoConfig reminderDaoConfig;

    private final ChannelProgramDao channelProgramDao;
    private final ProfileDao profileDao;
    private final ReminderDao reminderDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        channelProgramDaoConfig = daoConfigMap.get(ChannelProgramDao.class).clone();
        channelProgramDaoConfig.initIdentityScope(type);

        profileDaoConfig = daoConfigMap.get(ProfileDao.class).clone();
        profileDaoConfig.initIdentityScope(type);

        reminderDaoConfig = daoConfigMap.get(ReminderDao.class).clone();
        reminderDaoConfig.initIdentityScope(type);

        channelProgramDao = new ChannelProgramDao(channelProgramDaoConfig, this);
        profileDao = new ProfileDao(profileDaoConfig, this);
        reminderDao = new ReminderDao(reminderDaoConfig, this);

        registerDao(ChannelProgram.class, channelProgramDao);
        registerDao(Profile.class, profileDao);
        registerDao(Reminder.class, reminderDao);
    }
    
    public void clear() {
        channelProgramDaoConfig.clearIdentityScope();
        profileDaoConfig.clearIdentityScope();
        reminderDaoConfig.clearIdentityScope();
    }

    public ChannelProgramDao getChannelProgramDao() {
        return channelProgramDao;
    }

    public ProfileDao getProfileDao() {
        return profileDao;
    }

    public ReminderDao getReminderDao() {
        return reminderDao;
    }

}