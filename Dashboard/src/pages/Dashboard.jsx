import React, { useState, useEffect } from 'react';

import Sidebar from '../partials/Sidebar';
import Header from '../partials/Header';
import WelcomeBanner from '../partials/dashboard/WelcomeBanner';
import DashboardCard01 from '../partials/dashboard/DashboardCard01';
import DashboardCard02 from '../partials/dashboard/DashboardCard02';
import DashboardCard03 from '../partials/dashboard/DashboardCard03';
import DashboardCard04 from '../partials/dashboard/DashboardCard04';

function Dashboard() {

  const [sidebarOpen, setSidebarOpen] = useState(false);

  const [dashboardData, setDashboardData] = useState({
    totalSales: null,
    salesAmount: null,
    eventRevenue: {},
    eventSalesCount: {},
    customerInfo: {},
    dailyRevenue: {},
    dailySales: {}
  });

  useEffect(() => {
    const eventSource = new EventSource('http://localhost:8082/rtbi/sse');

    eventSource.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setDashboardData(prevData => ({
        ...prevData,
        totalSales: data.totalSales,
        salesAmount: data.salesAmount,
        eventRevenue: data.eventRevenue,
        eventSalesCount: data.eventSalesCount,
        customerInfo: data.customers,
        dailyRevenue: data.dailyRevenue,
        dailySales: data.dailySales,
      }));
    };

    eventSource.onerror = (error) => {
      console.error("SSE Connection Error: ", error);
    };
  
    return () => {
      eventSource.close();
    };
  }, []);

  return (
    <div className="flex h-screen overflow-hidden">

      {/* Sidebar */}
      <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />

      {/* Content area */}
      <div className="relative flex flex-col flex-1 overflow-y-auto overflow-x-hidden">

        {/*  Site header */}
        <Header sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />

        <main>
          <div className="px-4 sm:px-6 lg:px-8 py-8 w-full max-w-9xl mx-auto">

            {/* Welcome banner */}
            <WelcomeBanner />

            {/* Cards */}
            <div className="grid grid-cols-12 gap-6">

              {/*{/* Line chart (Real Time Value) */}
              <DashboardCard01 dailyRevenue={dashboardData.dailyRevenue}/>
              {/* Total Sales */}
              <DashboardCard02 dailySales={dashboardData.dailySales}/>
              {/* Table (Event) */}
              <DashboardCard03 eventRevenue={dashboardData.eventRevenue} eventSalesCount={dashboardData.eventSalesCount}/>
              {/* Table (Customers) */}
              <DashboardCard04 customerInfo={dashboardData.customerInfo}/>
              
            </div>

          </div>
        </main>

      </div>
    </div>
  );
}

export default Dashboard;