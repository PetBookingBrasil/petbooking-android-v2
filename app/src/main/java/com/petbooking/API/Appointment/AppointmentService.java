package com.petbooking.API.Appointment;

import android.util.Log;

import com.petbooking.API.APIClient;
import com.petbooking.API.Appointment.Models.CartResp;
import com.petbooking.API.Appointment.Models.CartRqt;
import com.petbooking.API.Appointment.Models.ProfessionalResp;
import com.petbooking.API.Appointment.Models.ServiceResp;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.User.Models.CreateUserRqt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.AppointmentDate;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.CartItem;
import com.petbooking.Models.Professional;
import com.petbooking.Models.User;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
        Map<String, String> map = new HashMap();
        Call<ServiceResp> call;
        map.put(APIAppointmentConstants.FIELDS_QUERY, APIAppointmentConstants.FIELDS_SERVICES);
        if (!petId.isEmpty())
            map.put(APIAppointmentConstants.PET_QUERY, petId);
        call = mAppointmentInterface.listCategoryServices(categoryId, map);


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
                    ArrayList<AppointmentDate> dates;
                    ProfessionalResp resp = response.body();

                    for (ProfessionalResp.Item item : resp.data) {
                        Professional professional = new Professional(item.id, item.attributes.name, item.attributes.gender,
                                item.attributes.avatar.avatar.url);

                        if (item.attributes.availableSlots.size() != 0) {
                            int dateIndex = -1;
                            String monthName;
                            int year;
                            dates = new ArrayList<>();

                            Calendar calendar = new GregorianCalendar();

                            for (ProfessionalResp.Slot slot : item.attributes.availableSlots) {
                                AppointmentDate date;
                                calendar.setTime(slot.date);
                                monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                                year = calendar.get(Calendar.YEAR);
                                dateIndex = AppUtils.containsMonth(dates, monthName, year);

                                if (dateIndex == -1) {
                                    date = new AppointmentDate(monthName, year);
                                    date.days.add(slot);
                                    dates.add(date);
                                } else {
                                    date = dates.get(dateIndex);
                                    date.days.add(slot);
                                }
                            }

                            professional.setAvailableDates(dates);
                        }

                        professionalList.add(professional);
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

    public void createCart(String userId, ArrayList<CartItem> itens, final APICallback callback) {
        CartRqt cartRqt = new CartRqt(itens);
        Call<CartResp> call = mAppointmentInterface.createCart(userId, cartRqt);
        call.enqueue(new Callback<CartResp>() {
            @Override
            public void onResponse(Call<CartResp> call, Response<CartResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<CartResp> call, Throwable t) {

            }
        });
    }
}
