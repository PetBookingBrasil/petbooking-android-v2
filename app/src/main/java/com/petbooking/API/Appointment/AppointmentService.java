package com.petbooking.API.Appointment;

import android.util.Log;

import com.google.gson.Gson;
import com.petbooking.API.APIClient;
import com.petbooking.API.Appointment.Models.ProfessionalResp;
import com.petbooking.API.Appointment.Models.ServiceResp;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Professional;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Luciano José on 29/07/2017.
 */

public class AppointmentService {

    private AppointmentInterface mAppointmentInterface;

    public AppointmentService() {
        mAppointmentInterface = APIClient.getClient().create(AppointmentInterface.class);
    }

    public void listServices(String categoryId, String petId, final APICallback callback) {
        Call<ServiceResp> call = mAppointmentInterface.listCategoryServices(categoryId, petId);
        call.enqueue(new Callback<ServiceResp>() {
            @Override
            public void onResponse(Call<ServiceResp> call, Response<ServiceResp> response) {
                if (response.isSuccessful()) {
                    ArrayList<BusinessServices> services = new ArrayList<BusinessServices>();
                    ServiceResp serviceResp = response.body();

                    for (ServiceResp.Item item : serviceResp.data) {
                        BusinessServices service = APIUtils.parseBusinessService(item);

                        if (item.attributes.additionalServices != null) {
                            for (ServiceResp.Additional additional : item.attributes.additionalServices) {
                                service.additionalServices.add(new BusinessServices(additional.id, additional.name,
                                        additional.duration, additional.description, additional.price));
                            }
                        }

                        services.add(service);
                    }

                    callback.onSuccess(services);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<ServiceResp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void listProfessional(String serviceId, final APICallback callback) {
        Call<ProfessionalResp> call = mAppointmentInterface.listProfessional(serviceId);
        call.enqueue(new Callback<ProfessionalResp>() {
            @Override
            public void onResponse(Call<ProfessionalResp> call, Response<ProfessionalResp> response) {
                if (response.isSuccessful()) {
                    ArrayList<Professional> professionalList = new ArrayList<>();
                    ProfessionalResp resp = response.body();

                    for (ProfessionalResp.Item professional : resp.data) {
                        professionalList.add(new Professional(professional.id, professional.attributes.name, professional.attributes.avatar));
                    }

                    callback.onSuccess(professionalList);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<ProfessionalResp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
