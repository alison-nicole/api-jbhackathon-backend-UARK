package com.jbhunt.infrastructure.universityhackathon.services;
import com.jbhunt.infrastructure.universityhackathon.data.dto.JudgeInfoDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Judge;
import com.jbhunt.infrastructure.universityhackathon.repository.JudgeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JudgeServiceTest {

    @Mock
    private JudgeRepository mockJudgeRepository;
    @InjectMocks
    private JudgeService judgeService;

    @Test
    public void getJudgesTest() {
        Judge judge1 = new Judge();
        judge1.setFirstName("bob");
        judge1.setLastName("gates");
        judge1.setExpirationTimestamp(new Date(System.currentTimeMillis() + 2000));
        judge1.setEffectiveTimestamp(new Date(System.currentTimeMillis()));

        Judge judge2 = new Judge();
        judge2.setFirstName("bill");
        judge2.setExpirationTimestamp(new Date(System.currentTimeMillis() + 2000));
        judge2.setEffectiveTimestamp(new Date(System.currentTimeMillis()));

        //  Test expired judge
        Judge judge3 = new Judge();
        judge3.setFirstName("billy");
        judge3.setExpirationTimestamp(new Date(System.currentTimeMillis() - 2000));
        judge3.setEffectiveTimestamp(new Date(System.currentTimeMillis()));

        List<Judge> judges = new ArrayList<>(Arrays.asList(judge1, judge2, judge3));
        when(mockJudgeRepository.findAll()).thenReturn(judges);
        List<String> actual = judgeService.getJudges();

        Assert.assertThat(actual.get(0), is("bob gates"));
        Assert.assertThat(actual.get(1), is("bill"));
        Assert.assertEquals(actual.size(), 2);
        verify(mockJudgeRepository).findAll();
        verifyNoMoreInteractions(mockJudgeRepository);
    }

    @Test
    public void createJudgeTest() {
        Date oldDate = new Date(System.currentTimeMillis() - 2000);
        Date newDate = new Date(System.currentTimeMillis());
        //  Testing to see service is properly updating judges info
        JudgeInfoDTO judgeInfoDTO = new JudgeInfoDTO();
        judgeInfoDTO.setFirstName("Billy");
        judgeInfoDTO.setLastName("Ross");

        Judge judge = new Judge();
        judge.setFirstName("Billy");
        judge.setLastName("Ross");
        judge.setExpirationTimestamp(oldDate);
        judge.setEffectiveTimestamp(oldDate);

        when(mockJudgeRepository.save(any())).thenReturn(judge);
        when(mockJudgeRepository.findJudgeByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.of(judge));
        Judge actual = judgeService.createJudge(judgeInfoDTO);
        Assert.assertEquals(actual.getFirstName(), judgeInfoDTO.getFirstName());
        Assert.assertEquals(actual.getLastName(), judgeInfoDTO.getLastName());
        verify(mockJudgeRepository).save(any());
        verify(mockJudgeRepository).findJudgeByFirstNameAndLastName(anyString(), anyString());
        verifyNoMoreInteractions(mockJudgeRepository);
    }

    @Test
    public void getJudgeByEmailWhenJudgeExists() {
        Judge judge = new Judge();
        judge.setFirstName("First");
        judge.setEmail("Email");

        when(mockJudgeRepository.findJudgeByEmail("Email")).thenReturn(Optional.of(judge));
        Optional<Judge> actual = judgeService.getJudgeByEmail("Email");

        Assert.assertEquals(actual.get().getEmail(), judge.getEmail());
        Assert.assertEquals(actual.get().getFirstName(), judge.getFirstName());
        verify(mockJudgeRepository).findJudgeByEmail("Email");
        verifyNoMoreInteractions(mockJudgeRepository);
    }

    @Test
    public void getJudgeByEmailWhenJudgeDoesNotExist() {
        when(mockJudgeRepository.findJudgeByEmail("Email")).thenReturn(Optional.empty());
        Optional<Judge> actual = judgeService.getJudgeByEmail("Email");

        Assert.assertEquals(actual, Optional.empty());
        verify(mockJudgeRepository).findJudgeByEmail("Email");
        verifyNoMoreInteractions(mockJudgeRepository);
    }
}

