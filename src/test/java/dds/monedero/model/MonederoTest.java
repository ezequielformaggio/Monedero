package dds.monedero.model;

import dds.monedero.exceptions.*;
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
    assertThrows(CuentaException.class, () -> cuenta.operar(-1500, new Deposito()));
  }

  @Test
  void NoSePuedenHacerMasDeTresDepositosPorDia() {
    assertThrows(CuentaException.class, () -> {
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1500, new Deposito());
    });
  }

  @Test
  void NoSePuedeExtraerUnMontoMayorAlMontoDisponible() {
    assertThrows(CuentaException.class, () -> {
      cuenta.operar(150, new Deposito());
      cuenta.operar(500, new Extraccion());
    });
  }

  @Test
  public void NoSePuedeExtraerMasDe1000PesosDiarios() {
    assertThrows(CuentaException.class, () -> {
      cuenta.operar(1500, new Deposito());
      cuenta.operar(1001, new Extraccion());
    });
  }

  @Test
  public void NoSePuedeExtraerUnMontoNegativo() {
    assertThrows(CuentaException.class, () -> cuenta.operar(-1500, new Extraccion()));
  }

}