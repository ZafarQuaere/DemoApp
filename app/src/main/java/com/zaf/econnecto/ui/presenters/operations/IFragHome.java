package com.zaf.econnecto.ui.presenters.operations;


import com.zaf.econnecto.network_call.response_model.home.Sales;

import java.util.List;

public interface IFragHome {
    void updateTodaySalesData(List<Sales> todaySalesData);
    void updateTotalSalesData(List<Sales> totalSalesData);
}
