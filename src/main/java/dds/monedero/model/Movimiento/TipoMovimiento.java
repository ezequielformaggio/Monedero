package dds.monedero.model.Movimiento;

import dds.monedero.model.Cuenta;

public interface TipoMovimiento {

  void impactarEnCuenta(Cuenta cuenta, double monto);

}
