package com.arca.equipfix.gambachanneltv.data.db;

import android.content.Context;

import com.arca.equipfix.gambachanneltv.data.models.ChannelProgramDao;
import com.arca.equipfix.gambachanneltv.data.models.DaoMaster;
import com.arca.equipfix.gambachanneltv.data.models.ReminderDao;
import com.arca.equipfix.gambachanneltv.di.ApplicationContext;
import com.arca.equipfix.gambachanneltv.di.DatabaseInfo;
import com.arca.equipfix.gambachanneltv.utils.AppLogger;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class DbOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    DbOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        AppLogger.d("DEBUG", "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);

        /*ChannelProgramDao.dropTable(db,true);
        ChannelProgramDao.createTable(db, true);
        ReminderDao.dropTable(db, true);
        ReminderDao.createTable(db, true);*/


        switch (oldVersion) {
            case 1:
            case 2:
                break;
        }
    }
}
