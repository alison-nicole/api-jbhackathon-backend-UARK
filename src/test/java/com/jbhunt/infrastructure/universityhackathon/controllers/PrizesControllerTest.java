package com.jbhunt.infrastructure.universityhackathon.controllers;

import com.jbhunt.infrastructure.universityhackathon.data.dto.PrizesDTO;
import com.jbhunt.infrastructure.universityhackathon.entity.Prizes;
import com.jbhunt.infrastructure.universityhackathon.mocks.PrizesMock;
import com.jbhunt.infrastructure.universityhackathon.mocks.PrizesDTOMock;
import com.jbhunt.infrastructure.universityhackathon.services.PrizesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrizesControllerTest {

    @Mock
    private PrizesService mockPrizesService;
    @InjectMocks
    private PrizesController prizesController;
    @Test
    public void testSavePrize(){
        //ARRANGE
        Prizes fakePrize = new Prizes();
        fakePrize.setPrizeName("prize name");
        fakePrize.setPrizeImageCode("prize image desc");
        fakePrize.setPrizeLink("prize link");
        fakePrize.setPrizeMonetaryValue("34.99");
        PrizesDTO prizesDTO = new PrizesDTO();
        when(mockPrizesService.savePrize(prizesDTO)).thenReturn(fakePrize);

        //ACT
        ResponseEntity<Prizes>actual = prizesController.savePrize(prizesDTO);

        //ASSERT
        Assert.assertThat(actual.getStatusCode(), is(HttpStatus.CREATED));
        Assert.assertThat(actual.getBody().getPrizeName(), is("prize name"));
        Assert.assertThat(actual.getBody().getPrizeLink(),is("prize link"));
        verify(mockPrizesService).savePrize((any()));

    }

    @Test
    public void getAllPrizesTest(){
        //ARRANGE
        Prizes fakePrize1 = PrizesMock.getPrizeMock();
        Prizes fakePrize2 = PrizesMock.getPrizeMock();
        fakePrize2.setPrizeID(2);
        List<Prizes> fakePrizes = new ArrayList<>(Arrays.asList(fakePrize1,fakePrize2));
        when(mockPrizesService.getAllPrizes()).thenReturn(fakePrizes);

        //ACT
        List<Prizes> actual = prizesController.getAllPrizes();

        //ASSERT
        Assert.assertEquals(actual.size(),2);
        Assert.assertEquals(fakePrizes,actual);
        verify(mockPrizesService).getAllPrizes();
    }
}
