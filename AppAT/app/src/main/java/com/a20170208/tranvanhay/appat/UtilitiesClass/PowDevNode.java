package com.a20170208.tranvanhay.appat.UtilitiesClass;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Van Hay on 01-Jun-17.
 */

public class PowDevNode {
    private static final String TAG = PowDevNode.class.getSimpleName();
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private String MACAddr; // help server recognize
    private String ID;      // help user recognize
    private int strengthWifi;
    private boolean dev0, dev1, buzzer, sim0, sim1;
    private boolean lastCorrectDev0, lastCorrectDev1, lastCorrectBuzzer;
    private String timeOperation;
    private int zone;       // group sensors  and powdev nodes into zones
    private boolean isEnable;

    public PowDevNode() {
    }

    public PowDevNode(String MACAddr, String ID, int strengthWifi, boolean dev0, boolean dev1,
                      boolean buzzer, boolean sim0, boolean sim1, String timeOperation, boolean isEnable) {
        this.MACAddr = MACAddr;
        this.ID = ID;
        this.strengthWifi = strengthWifi;
        this.dev0 = dev0;
        this.dev1 = dev1;
        this.buzzer = buzzer;
        this.sim0 = sim0;
        this.sim1 = sim1;
        this.timeOperation = timeOperation;
        this.isEnable = isEnable;
    }

    public String getMACAddr() {
        return MACAddr;
    }

    public void setMACAddr(String MACAddr) {
        this.MACAddr = MACAddr;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getStrengthWifi() {
        return strengthWifi;
    }

    public void setStrengthWifi(int strengthWifi) {
        this.strengthWifi = strengthWifi;
    }

    public boolean isDev0() {
        return dev0;
    }

    public void setDev0(boolean dev0) {
        this.dev0 = dev0;
    }

    public boolean isDev1() {
        return dev1;
    }

    public void setDev1(boolean dev1) {
        this.dev1 = dev1;
    }

    public boolean isBuzzer() {
        return buzzer;
    }

    public void setBuzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    public boolean isSim0() {
        return sim0;
    }

    public void setSim0(boolean sim0) {
        this.sim0 = sim0;
    }

    public boolean isSim1() {
        return sim1;
    }

    public void setSim1(boolean sim1) {
        this.sim1 = sim1;
    }

    public boolean isLastCorrectDev0() {
        return lastCorrectDev0;
    }

    public void setLastCorrectDev0(boolean lastCorrectDev0) {
        this.lastCorrectDev0 = lastCorrectDev0;
    }

    public boolean isLastCorrectDev1() {
        return lastCorrectDev1;
    }

    public void setLastCorrectDev1(boolean lastCorrectDev1) {
        this.lastCorrectDev1 = lastCorrectDev1;
    }

    public boolean isLastCorrectBuzzer() {
        return lastCorrectBuzzer;
    }

    public void setLastCorrectBuzzer(boolean lastCorrectBuzzer) {
        this.lastCorrectBuzzer = lastCorrectBuzzer;
    }

    public String getTimeOperation() {
        return timeOperation;
    }

    public void setTimeOperation(String timeOperation) {
        this.timeOperation = TimeAndDate.convertMilisecondToTimeAndDate(timeOperation);
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
        mData.child(FirebasePath.POWDEV_DETAILS_PATH).child(ID).child("isEnable").setValue(isEnable);
        Log.d(TAG,"Set enable");
    }

    @Override
    public String toString() {
        return "PowDevNode{" +
                "MACAddr='" + MACAddr + '\'' +
                ", ID='" + ID + '\'' +
                ", strengthWifi=" + strengthWifi +
                ", dev0=" + dev0 +
                ", dev1=" + dev1 +
                ", buzzer=" + buzzer +
                ", sim0=" + sim0 +
                ", sim1=" + sim1 +
                '}';
    }
}
