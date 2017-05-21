package com.petbooking.Constants;

import java.util.HashMap;

/**
 * Created by Luciano José on 19/05/2017.
 */

public class States {

    public HashMap<String, String> states;

    public States() {
        states = new HashMap<>();
        states.put("Acre", "AC");
        states.put("Alagoas", "AL");
        states.put("Amapá", "AP");
        states.put("Amazonas", "AM");
        states.put("Bahia", "BA");
        states.put("Ceará", "CE");
        states.put("Distrito Federal", "DF");
        states.put("Espírito Santo", "ES");
        states.put("Goiás", "GO");
        states.put("Maranhão", "MA");
        states.put("Mato Grosso", "MT");
        states.put("Mato Grosso do Sul", "MS");
        states.put("Minas Gerais", "MG");
        states.put("Pará", "PA");
        states.put("Paraíba", "PB");
        states.put("Paraná", "PR");
        states.put("Pernambuco", "PE");
        states.put("Piauí", "PI");
        states.put("Rio de Janeiro", "RJ");
        states.put("Rio Grande do Norte", "RN");
        states.put("Rio Grande do Sul", "RS");
        states.put("Rondônia", "RO");
        states.put("Roraima", "RR");
        states.put("Santa Catarina", "SC");
        states.put("São Paulo", "SP");
        states.put("Sergipe", "SE");
        states.put("Tocantins", "TO");
    }

    public String getAbreviation(String state) {
        return states.get(state);
    }
}
