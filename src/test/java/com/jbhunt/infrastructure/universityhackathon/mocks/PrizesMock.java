package com.jbhunt.infrastructure.universityhackathon.mocks;

import com.jbhunt.infrastructure.universityhackathon.entity.Prizes;
import java.util.ArrayList;
import java.util.List;
public class PrizesMock {



    public static Prizes getPrizeMock(){
        Prizes prize1 = new Prizes();
        prize1.setPrizeName("prize1 name");
        prize1.setPrizeMonetaryValue("prize1 value");
        prize1.setPrizeLink("prize1 link");
        prize1.setPrizeImageCode("prize1 image code");
        prize1.setPrizeID(1);
        return prize1;
    }
    public static List<Prizes> getPrizesList(){
        List<Prizes> prizesList = new ArrayList<>();

        Prizes prize1 = new Prizes();
        prize1.setPrizeName("prize1 name");
        prize1.setPrizeMonetaryValue("prize1 value");
        prize1.setPrizeLink("prize1 link");
        prize1.setPrizeImageCode("prize1 image code");
        prize1.setPrizeID(1);

        Prizes prize2 = new Prizes();
        prize2.setPrizeName("prize2 name");
        prize2.setPrizeMonetaryValue("prize2 value");
        prize2.setPrizeLink("prize2 link");
        prize2.setPrizeImageCode("prize2 image code");
        prize2.setPrizeID(2);

        prizesList.add(prize1);
        prizesList.add(prize2);
        return prizesList;
    }
}
