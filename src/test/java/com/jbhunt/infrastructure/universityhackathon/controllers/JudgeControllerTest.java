package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgeInfoDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Judge;
import com.jbhunt.infrastructure.universityhackathon.services.JudgeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JudgeControllerTest {
    @Mock
    private JudgeService mockJudgeService;

    @InjectMocks
    private JudgeController judgeController;

    @Test
    public void getAllJudgesTest() {
        List<String> mockResult = new ArrayList<>();
        mockResult.add("judge 1");
        when(mockJudgeService.getJudges()).thenReturn(mockResult);

        ResponseEntity<List<String>> actual = judgeController.getAllJudges();

        Assert.assertEquals(mockResult, actual.getBody());
        verify(mockJudgeService).getJudges();
    }

    @Test
    public void createNewJudgeTest() {
        Judge judge = new Judge();
        judge.setFirstName("Bob");
        judge.setLastName("Ross");

        JudgeInfoDTO judgeInfoDTO = new JudgeInfoDTO();
        judgeInfoDTO.setFirstName("Bob");
        judgeInfoDTO.setLastName("Ross");

        when(mockJudgeService.createJudge(any())).thenReturn(judge);

        ResponseEntity<Judge> actual = judgeController.createJudge(judgeInfoDTO);

        Assert.assertEquals(judge, actual.getBody());
        verify(mockJudgeService).createJudge(any());
    }

    @Test
    public void testJudgeExistsWhenJudgeExists() {
        Judge judge = new Judge();
        judge.setEmail("Email");

        when(mockJudgeService.getJudgeByEmail(any())).thenReturn(Optional.of(judge));

        ResponseEntity<Boolean> actual = judgeController.judgeExists("Email");

        Assert.assertEquals(true, actual.getBody());
        verify(mockJudgeService).getJudgeByEmail("Email");
    }

    @Test
    public void testJudgeExistsWhenJudgeDoesNotExist() {
        when(mockJudgeService.getJudgeByEmail(any())).thenReturn(Optional.empty());

        ResponseEntity<Boolean> actual = judgeController.judgeExists("Email");

        Assert.assertEquals(false, actual.getBody());
        verify(mockJudgeService).getJudgeByEmail("Email");
    }
}

