package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.dto.PrizesDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.HackathonEvent;
import com.jbhunt.infrastructure.universityhackathon.repository.PrizesRepository;
import  com.jbhunt.infrastructure.universityhackathon.entity.Prizes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrizesService {
    private final PrizesRepository prizesRepository;
    private final HackathonEventService hackathonEventService;


    public Prizes createPrize(String prizeName){
        var prize = Prizes.builder()
                .prizeName(prizeName)
                .build();
        int hackathonEventID = -1;
        List<HackathonEvent> current = hackathonEventService.getCurrentHackathon();
        if(!current.isEmpty()) {
            hackathonEventID = current.get(0).getHackathonEventID();

            prize.setPrizeName(prizeName);
            prize.setHackathonEventID(hackathonEventID);


            return prizesRepository.save(prize);
        } else {
            log.error("HACKATHON DOESN'T EXIST");
            throw new NullPointerException("NO HACKATHON EVENT DATA");
        }
    }
    public Prizes createPrize(String prizeName, String prizeLink, String prizeMonetaryValue, String prizeImageCode){
        var prize = Prizes.builder()
                .prizeName(prizeName)
                .prizeMonetaryValue(prizeMonetaryValue)
                .prizeLink(prizeLink)
                .prizeImageCode(prizeImageCode)
                .build();
        int hackathonEventID = -1;
        List<HackathonEvent> current = hackathonEventService.getCurrentHackathon();
        if(!current.isEmpty()) {
            hackathonEventID = current.get(0).getHackathonEventID();


            return prizesRepository.save(prize);
        } else {
            log.error("HACKATHON DOESN'T EXIST");
            throw new NullPointerException("NO HACKATHON EVENT DATA");
        }
    }



    public Prizes savePrize(PrizesDTO administratorRequest) {
        //TODO: should the request be validated/trimmed as the signup request (in signupService)
        var prize = createPrizeFromForm(administratorRequest);
        prize = prizesRepository.save(prize);

        return prize;
    }

    Prizes createPrizeFromForm(PrizesDTO administratorRequest) {
        var prize = new Prizes();
        prize.setPrizeName(administratorRequest.getPrizeName());
        prize.setPrizeMonetaryValue(administratorRequest.getPrizeMonetaryValue());
        prize.setPrizeLink(administratorRequest.getPrizeLink());
        prize.setPrizeImageCode(administratorRequest.getPrizeImageCode()); //Note: this saves an 'alias' for the image. if the image is a smartwatch , the alias will be a string 'smartwatch'
        prize.setHackathonEventID(hackathonEventService.getCurrentHackathon().get(0).getHackathonEventID());
        return prize;
    }
    public boolean getByPrizeName(String prizeName){
        try{
            Optional<Prizes> prize = prizesRepository.findPrizeByPrizeNameIgnoreCase(prizeName);
            //TODO: remove logs
            log.info(prizeName);
            log.info(prize.get().getPrizeName());
            return prize.isPresent();
        }
        catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean removePrizeByPrizeName(String prizeName){
        try{
            Optional<Prizes> prizesOptional = prizesRepository.findPrizeByPrizeName(prizeName);
            if(prizesOptional.isPresent()){
                prizesOptional.ifPresent(prizesRepository::delete);
                return true;
            }
        }
        catch (Exception e){
            log.info("prize does not exist");
            log.error(e.getMessage());
            return false;
        }
        return false;
    }
    public boolean removeAllPrizes() {
        prizesRepository.deleteAll();
        return true;
    }

    public List<Prizes> getAllPrizes() {
        return prizesRepository.findAll();
    }

    public Prizes updatePrize(String prizeName, PrizesDTO currentPrize) {
        Optional<Prizes> prizesOptional = prizesRepository.findPrizeByPrizeName(prizeName);
        log.info("current prize name = "+ currentPrize.getPrizeName());
        if (prizesOptional.isPresent()){
            var prize = prizesOptional.get();
            prize.setPrizeName(currentPrize.getPrizeName());
            prize.setPrizeLink(currentPrize.getPrizeLink());
            prize.setPrizeImageCode(currentPrize.getPrizeImageCode());
            prize.setPrizeMonetaryValue(currentPrize.getPrizeMonetaryValue());
            return prizesRepository.save(prize);
        }
        return null;
    }



//    public List<Prizes> getAllByHackathonEventID(int hackathonEventID){
//        return prizesRepository.findAllByHackathonEventID(hackathonEventID);
//    }
//
//    public String  getPrizeByMonetaryValue(String prizeMonetaryValue){
//
//    }
}
