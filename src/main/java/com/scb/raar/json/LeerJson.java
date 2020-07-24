package com.scb.raar.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author raruiz
 */
public class LeerJson {
    String file;

    public LeerJson(String file) {
        this.file = file;
    }

    public void readJson() {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();

            String content = stringBuilder.toString();

            JSONArray jsonArray = new JSONArray(content);
            List<String> ciudades = new ArrayList<>();
            List<String> meses = new ArrayList<>();

            jsonArray.forEach((object) -> {
                JSONObject config = (JSONObject) object;
                ciudades.add(config.get("ciudad").toString());
                meses.add(config.get("mes").toString());
            });
            List<String> distinctCities = ciudades.stream().distinct().collect(Collectors.toList());
            List<String> distinctMonths = meses.stream().distinct().collect(Collectors.toList());

            distinctCities.forEach(city -> {
                distinctMonths.forEach(month -> {
                    final BigDecimal[] sum = {BigDecimal.ZERO};
                    jsonArray.forEach((object) -> {
                        JSONObject config = (JSONObject) object;
                        if (config.get("ciudad").toString().equals(city) && config.get("mes").toString().equals(month)){
                            BigDecimal val = new BigDecimal(config.get("venta").toString());
                            sum[0] = sum[0].add(val);
                        }
                    });
                    System.out.println("La ciudad " + city + " tuvo un valor de ventas de: $" + sum[0] + " durante el mes " +
                            "de " + month);
                });
            });

        } catch (Exception e) {
            System.out.println("Excepcion leyendo fichero json: " + e.getMessage());
        }
    }

}
