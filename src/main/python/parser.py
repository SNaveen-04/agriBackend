import sys
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select, WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
commodity_name = sys.argv[1]

driver.get("https://agmarknet.gov.in/")

# Wait for the close button to be clickable
close_button = WebDriverWait(driver, 10).until(
    EC.element_to_be_clickable((By.CLASS_NAME, "close"))
)

# Click the close button
close_button.click()

# Select commodity
commodity_dropdown = Select(driver.find_element(By.ID, "ddlCommodity"))
commodity_dropdown.select_by_visible_text(commodity_name)

# Select state
state_dropdown = Select(driver.find_element(By.ID, "ddlState"))
state_dropdown.select_by_visible_text("Tamil Nadu")

# Click the "Go" button
go_button = driver.find_element(By.ID, "btnGo")
go_button.click()

# Wait until the results table appears
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "cphBody_GridPriceData"))
)

# Parse the page
soup = BeautifulSoup(driver.page_source, "html.parser")

def parse_table(soup):
    table = soup.find("table", id="cphBody_GridPriceData")
    data = []
    if table:
        rows = table.find_all("tr")
        for row in rows[1:]:  # skip header
            cols = row.find_all("td")
            cols = [ele.text.strip() for ele in cols]
            data.append(cols)
    return data

market_data = parse_table(soup)

# Print the data for all the district 
# for row in market_data:
#     print(row)



# Get the first row(Dharmapuri)
# first_row = market_data[0]

# # Get the value before the date
# value_before_date = first_row[-2]


# # Convert the value to an integer and divide by 100
# value_before_date = int(value_before_date.replace(',', ''))  # Remove any commas and convert to integer
# result = value_before_date // 100

# # Print the result
# print(result)


#for Coimbatore district
# Initialize variable to store the result
result = None

# Iterate over each row to find Coimbatore
for row in market_data:
    if len(row) > 0 and "Coimbatore" in row[1]:  # Assuming district name is in second column (index 1)
        value_before_date = row[-2]  # Get second last column value
        value_before_date = int(value_before_date.replace(',', ''))  # Remove commas and convert to int
        result = value_before_date // 100  # Perform your calculation
        break  # Stop after finding the first matching row

# Check if result was found
if result is not None:
    print(result)
else:
    print("No data found for Coimbatore")

driver.quit()
