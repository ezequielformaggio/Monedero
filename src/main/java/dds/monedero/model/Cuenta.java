package dds.monedero.model;

import dds.monedero.exceptions.*;
import dds.monedero.model.Movimiento.Movimiento;
import dds.monedero.model.Movimiento.TipoMovimiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private final List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void operar(double monto, TipoMovimiento tipo) {
    chequearMontoNegativo(monto);
    tipo.impactarEnCuenta(this,monto);
    agregarMovimiento(LocalDate.now(), monto, tipo);
  }

  public void agregarMovimiento(LocalDate fecha, double monto, TipoMovimiento tipo) {
    Movimiento movimiento = new Movimiento(fecha, monto, tipo);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoAFecha(LocalDate fecha) {
    return movimientos.stream()
        .filter(movimiento -> !movimiento.esDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public double getSaldo() {
    return saldo;
  }

  public void extraerSaldo(double monto) {
    this.saldo -= monto;
  }

  public void depositarSaldo(double monto) {
    this.saldo += monto;
  }

  public void chequearMontoNegativo(double monto) {
    if (monto < 0) {
      throw new CuentaException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void chequearCantidadDepositosDiarios() {
    if (movimientos.stream().filter(Movimiento::esDeposito).count() >= 3) {
      throw new CuentaException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void chequearMontoExtraccionDisponible(double monto) {
    if (saldo - monto < 0) {
      throw new CuentaException("No puede sacar mas de " + saldo + " $");
    }
  }

  public void chequearLimiteExtraccionDiario(double monto) {
    double limite = getMontoDeExtraccionPosibleHoy();
    if (monto > limite) {
      throw new CuentaException("No puede extraer mas de $ " + 1000
              + " diarios, l??mite: " + limite);
    }
  }

  public double getMontoDeExtraccionPosibleHoy() {
    return 1000 - getMontoExtraidoAFecha(LocalDate.now());
  }

}
