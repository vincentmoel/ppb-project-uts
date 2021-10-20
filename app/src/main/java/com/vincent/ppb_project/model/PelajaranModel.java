package com.vincent.ppb_project.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PelajaranModel implements Parcelable {
    private String matpel = "",
    materi = "",
    kelas = "",
    hari = "",
    id = "";

    private long start_at = 0,
    finish_at = 0,
    kuota = 0;

    public PelajaranModel(
            String matpel,
            String materi,
            String kelas,
            String hari,
            long start_at,
            long finish_at,
            long kuota
    ) {
      setMatpel(matpel);
      setMateri(materi);
      setKelas(kelas);
      setHari(hari);
      setStart_at(start_at);
      setFinish_at(finish_at);
      setKuota(kuota);
    }

    public PelajaranModel() {};


    protected PelajaranModel(Parcel in) {
        matpel = in.readString();
        materi = in.readString();
        kelas = in.readString();
        hari = in.readString();
        id = in.readString();
        start_at = in.readLong();
        finish_at = in.readLong();
        kuota = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(matpel);
        dest.writeString(materi);
        dest.writeString(kelas);
        dest.writeString(hari);
        dest.writeString(id);
        dest.writeLong(start_at);
        dest.writeLong(finish_at);
        dest.writeLong(kuota);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PelajaranModel> CREATOR = new Creator<PelajaranModel>() {
        @Override
        public PelajaranModel createFromParcel(Parcel in) {
            return new PelajaranModel(in);
        }

        @Override
        public PelajaranModel[] newArray(int size) {
            return new PelajaranModel[size];
        }
    };

    public String getMatpel() {
        return matpel;
    }

    public void setMatpel(String matpel) {
        this.matpel = matpel;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public long getStart_at() {
        return start_at;
    }

    public void setStart_at(long start_at) {
        this.start_at = start_at;
    }

    public long getFinish_at() {
        return finish_at;
    }

    public void setFinish_at(long finish_at) {
        this.finish_at = finish_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getKuota() {
        return kuota;
    }

    public void setKuota(long kuota) {
        this.kuota = kuota;
    }

}
