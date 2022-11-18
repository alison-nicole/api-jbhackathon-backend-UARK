package com.jbhunt.infrastructure.universityhackathon.services;

import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgeInfoDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Judge;
import com.jbhunt.infrastructure.universityhackathon.repository.JudgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JudgeService {

    private final JudgeRepository judgeRepository;

    public List<String> getJudges() {
        List<String> judgeNames = new ArrayList<>();
        List<Judge> judges =  judgeRepository.findAll();
        for(Judge currentJudge: judges)
        {
            var currentDate = new Date(System.currentTimeMillis());
            if(currentDate.after(currentJudge.getExpirationTimestamp())) {
                continue;
            }
            var judgeName = new StringBuilder();
            judgeName.append(currentJudge.getFirstName());
            if(currentJudge.getLastName() != null) {
                judgeName.append(" ").append(currentJudge.getLastName());
            }
            judgeNames.add(judgeName.toString());
        }
        return judgeNames;
    }

    public Optional<Judge> getJudgeByEmail(String email) {
        return judgeRepository.findJudgeByEmail(email);
    }

    public Judge createJudge(JudgeInfoDTO judgeInfo) {
        var judge = new Judge();
        long now = System.currentTimeMillis();

        judge.setFirstName(judgeInfo.getFirstName());
        judge.setLastName(judgeInfo.getLastName());
        judge.setEmail(judgeInfo.getEmail());
        judge.setEffectiveTimestamp(new Date(now));

        Optional<Judge> previousJudgeInfo = judgeRepository.findJudgeByFirstNameAndLastName(judgeInfo.getFirstName(), judgeInfo.getLastName());
        previousJudgeInfo.ifPresent(value -> judge.setJudgeID(value.getJudgeID()));

        return judgeRepository.save(judge);
    }
}
