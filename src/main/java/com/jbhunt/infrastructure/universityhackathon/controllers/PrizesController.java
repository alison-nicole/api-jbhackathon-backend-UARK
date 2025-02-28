package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.PrizesDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Prizes;
import static com.jbhunt.infrastructure.universityhackathon.constants.HackathonConstants.PRIZES;
import com.jbhunt.infrastructure.universityhackathon.repository.PrizesRepository;
import com.jbhunt.infrastructure.universityhackathon.services.PrizesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class PrizesController {
    private final PrizesService prizesService;
    private final PrizesRepository prizesRepository;

    //store a new prize
    @ResponseBody
    @PostMapping("/prizes")
    public ResponseEntity<Prizes> savePrize(@Valid @RequestBody PrizesDTO newPrize){
        try{
            Prizes prize = prizesService.savePrize(newPrize);
            return new ResponseEntity<>(prize, HttpStatus.CREATED);

        }
        catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    //get all prizes available
    @GetMapping("/all")
    public @ResponseBody List<Prizes> getAllPrizes(){
        return prizesService.getAllPrizes();
    }

    //get prize by prize name
    @GetMapping("/getPrizeName/{prizeName}")
    public @ResponseBody
    boolean getPrizeByPrizeName(@PathVariable(name="prizeName") String prizeName){
        return prizesService.getByPrizeName(prizeName);
    }

    //modify prize by prize name
    @PutMapping("/updatePrize/{prizeName}")
    public @ResponseBody
    ResponseEntity<Prizes>updatePrizeByPrizeName(@Valid @RequestBody PrizesDTO currentPrize, @PathVariable(name = "prizeName") String prizeName){
        return new ResponseEntity<>(prizesService.updatePrize(prizeName, currentPrize), HttpStatus.OK);
    }

    @DeleteMapping("/removePrize/{prizeName}")
    public @ResponseBody
    boolean removePrizeByPrizeName(@PathVariable(name = "prizeName")String prizeName){
        return prizesService.removePrizeByPrizeName(prizeName);
    }

    @DeleteMapping("/removeAllPrizes")
    public @ResponseBody
    boolean removeAllPrizes(){
        return prizesService.removeAllPrizes();
    }

}
