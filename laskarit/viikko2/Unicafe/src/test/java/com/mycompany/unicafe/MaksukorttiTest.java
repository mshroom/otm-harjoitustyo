package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoOnAlussaOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(50);
        assertEquals(60, kortti.saldo());
    }
    
    @Test
    public void rahanOttaminenVahentaaSaldoa() {
        boolean ret = kortti.otaRahaa(5);
        assertEquals(5, kortti.saldo());
        assertTrue(ret);
    }
    
    @Test
    public void rahaaEiOtetaJosSaldoaOnLiianVahan() {
        boolean ret = kortti.otaRahaa(15);
        assertEquals(10, kortti.saldo());
        assertFalse(ret);
    }
    
    @Test
    public void toStringToimiiOikein() {
        kortti.lataaRahaa(10);
        assertEquals("saldo: 0.20", kortti.toString());
    }
}
