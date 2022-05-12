package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import dds.monedero.model.Movimiento.Deposito;
import dds.monedero.model.Movimiento.Extraccion;
import dds.monedero.model.Movimiento.Movimiento;
import dds.monedero.model.Movimiento.TipoMovimiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void operar(double monto, TipoMovimiento operacion) {

    chequearMontoNegativo(monto);

    operacion.impactarEnCuenta(this,monto);

    agregarMovimiento(LocalDate.now(), monto, new Deposito());
  }

  public void poner(double monto) {

    chequearMontoNegativo(monto);

    chequearCantidadDepositosDiarios(monto);

    agregarMovimiento(LocalDate.now(), monto, new Deposito());

  }

  public void sacar(double monto) {

    chequearMontoNegativo(monto);

    chequearMontoExtraccionDisponible(monto);

    chequearLimiteExtraccionDiario(monto);

    agregarMovimiento(LocalDate.now(), monto, new Extraccion());
  }



  public void agregarMovimiento(LocalDate fecha, double monto, TipoMovimiento tipo) {
    Movimiento movimiento = new Movimiento(fecha, monto, tipo);
    tipo.impactarEnCuenta(this, monto);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return movimientos.stream()
        .filter(movimiento -> !movimiento.esDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
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
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void chequearCantidadDepositosDiarios(double monto) {
    if (movimientos.stream().filter(Movimiento::esDeposito).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void chequearMontoExtraccionDisponible(double monto) {
    if (saldo - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + saldo + " $");
    }
  }

  public void chequearLimiteExtraccionDiario(double monto) {
    double limite = getMontoDeExtraccionPosibleHoy();
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
              + " diarios, límite: " + limite);
    }
  }

  public double getMontoDeExtraccionPosibleHoy() {
    return 1000 - getMontoExtraidoA(LocalDate.now());
  }

}
