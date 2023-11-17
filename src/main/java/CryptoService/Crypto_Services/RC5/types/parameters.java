package CryptoService.Crypto_Services.RC5.types;

public enum parameters {
  p_sixteen(new int[]{183, 225}), //B7E1
  q_sixteen(new int[]{158, 55}), //9E37

  p_thirtyTwo(new int[]{183, 225, 81, 99}), //B7E15163
  q_thirtyTwo(new int[]{158, 55, 121, 185}), //9E3779B9

  p_sixtyFour(new int[]{183, 225, 81, 98, 138, 237, 42, 107}), //B7E151628AED2A6B
  q_sixtyFour(new int[]{158, 55, 121, 185, 127, 74, 124, 21}); //9E3779B97F4A7C15

  parameters(int[] magic_const) {
  }
}

