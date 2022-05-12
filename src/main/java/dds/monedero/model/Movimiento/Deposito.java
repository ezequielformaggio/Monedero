package dds.monedero.model.Movimiento;

import dds.monedero.model.Cuenta;

public class Deposito implements TipoMovimiento{

  public void impactarEnCuenta(Cuenta cuenta, double monto) {
    cuenta.depositarSaldo(monto);
  }
}