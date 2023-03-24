package jpabook.jpashop.domain;

//import javax.persistence.DiscriminatorColumn;
//import javax.persistence.DiscriminatorValue;
//import hellojpa.Item;

import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;
    private String etc;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }
}
