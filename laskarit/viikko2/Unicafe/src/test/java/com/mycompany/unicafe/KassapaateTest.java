package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {

    Kassapaate kassa;
    Maksukortti kortti;

    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(500);
    }

    @Test
    public void kassanTiedotAlussaOikein() {
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty() + kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void kateisostoToimiiEdullisellaLounaallaKunMaksuRiittava() {
        assertEquals(260, kassa.syoEdullisesti(500));
        assertEquals(100240, kassa.kassassaRahaa());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void kateisostoToimiiMaukkaallaLounaallaKunMaksuRiittava() {
        assertEquals(100, kassa.syoMaukkaasti(500));
        assertEquals(100400, kassa.kassassaRahaa());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void edullistaLounastaEiMyydaKunKateistaLiianVahan() {
        assertEquals(200, kassa.syoEdullisesti(200));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukastaLounastaEiMyydaKunKateistaLiianVahan() {
        assertEquals(200, kassa.syoMaukkaasti(200));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void korttiostoToimiiEdullisellaLounaallaKunSaldoRiittava() {
        assertTrue(kassa.syoEdullisesti(kortti));
        assertEquals(260, kortti.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void korttiostoToimiiMaukkaallaLounaallaKunSaldoRiittava() {
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertEquals(100, kortti.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void edullistaLounastaEiMyydaKunKortillaSaldoaLiianVahan() {
        kortti.otaRahaa(400);
        assertFalse(kassa.syoEdullisesti(kortti));
        assertEquals(100, kortti.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukastaLounastaEiMyydaKunKortillaSaldoaLiianVahan() {
        kortti.otaRahaa(400);
        assertFalse(kassa.syoMaukkaasti(kortti));
        assertEquals(100, kortti.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kortilleRahanLataaminenToimii() {
        kassa.lataaRahaaKortille(kortti, 500);
        assertEquals(1000, kortti.saldo());
        assertEquals(100500, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortilleEiLadataNegatiivistaSummaa() {
        kassa.lataaRahaaKortille(kortti, -10);
        assertEquals(500, kortti.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
