package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import dds.monedero.model.Movimiento.Deposito;
import dds.monedero.model.Movimiento.Extraccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void SePuedeHacerUnDepositoEnUnaCuentaSiEsteCumpleLasCondiciones() {
    assertEquals(0, cuenta.getSaldo());
    cuenta.operar( 1500, new Deposito());
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void NoSePuedeRealizarUnDepositoPorUnMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.operar(-1500, new Deposito()));
  }

  @Test
  void NoSePuedenHacerMasDeTresDepositosPorDia() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1500, new Deposito());
    });
  }

  @Test
  void NoSePuedeExtraerUnMontoMayorAlMontoDisponible() {
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.operar(150, new Deposito());
      cuenta.operar(500, new Extraccion());
    });
  }

  @Test
  public void NoSePuedeExtraerMasDe1000PesosDiarios() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1001, new Extraccion());
    });
  }

  @Test
  public void NoSePuedeExtraerUnMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.operar(-1500, new Extraccion()));
  }

}