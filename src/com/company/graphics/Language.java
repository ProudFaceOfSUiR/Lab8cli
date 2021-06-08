package com.company.graphics;

import com.company.enums.Languages;

public class Language {
    Languages currentLang;
    int language_code = 0;

    //login panel
    String[] loginLabel = {"Enter login: ", "Введите логин: ", "Введите логин: ", "Iveskite prisijungimo vardą: "};
    String[] passLabel = {"Enter password: ", "Введите пароль: ", "Введите пароль: ", "Ivesti slaptažodį: "};
    String[] repeatPassLabel = {"Repeat password: ", "Повторите пароль", "Повторите пароль","Pakartokite slaptažodį"};
    String[] loginButton = {"Log in", "Войти", "Войти", "Prisijungti"};
    String[] registerButton = {"Register instead", "Зарегистрироваться", "Зарегистрироваться", "Registruokis"};

    //database panel
    String[] addButton = {"Add worker", "Добавить работника", "Добавить работника", "Pridėti darbuotoją"};
    String[] removeButton = {"Remove seleccted", "Удалить выбранного", "Удалить выбранного", "Pašalinti pasirinktus"};
    String[] removeGreaterButton = {"Remove greater", "Удалить больших",  "Удалить больших", "Pašalinti didesnį"};
    String[] removeLowerButton = {"Remove lower", "Удалить меньших", "Удалить меньших", "Nuimkite apatinę"};
    String[] updateButton = {"Update selected", "Изменить", "Изменить", "Pasirinktas atnaujinimas"};
    String[] clearButton = {"Clear database", "Очистить БД", "Очистить БД", "Išvalyti duomenų bazę"};
    String[] visButton = {"Visualisation", "Визуализация","Визуализация", "Vizualizacija"};
    String[] refreshButton = {"Refresh", "Обновить","Обновить","Atnaujinti"};

    //add worker panel
    String[] nameLabel = {"Name: ", "Имя: ","Имя: ","Vardas"};
    String[] salaryLabel = {"Salary: ", "Зарплата: ", "Зарплата: ", "Atlyginimas: "};
    String[] positionLabel = {"Position: ", "Должность: ", "Должность: ", "Poziciją: "};
    String[] personLabel = {"Personality: ", "Личность: ", "Личность: ", "Asmenybė: "};
    String[] coordLabel = {"Coordinates: ", "Координаты: ", "Координаты: ", "Koordinatės: "};
    String[] startDateLabel = {"Start date: ", "Первый день: ", "Первый день: ", "Pradžios data: "};
    String[] endDateLabel = {"End date: ", "Последний день: ", "Последний день: ", "Pabaigos data: "};
    String[] addIfMaxLabel = {"Add if salary is max", "Добавить, если зп максимальная", "Добавить, если зп максимальная", "Pridėkite, jei atlyginimas yra maksimalus"};
    String[] submitButton = {"Submit", "Отправить", "Отправить", "Pateikti"};
    String[] backButton = {"Back", "Назад", "Назад", "Atgal"};

    public Languages getCurrentLang() {
        return currentLang;
    }

    public void setLanguage(Languages language){
        currentLang = language;
        switch (language){
            case en_NZ:
                language_code = 0;
                break;
            case ru_RU:
                language_code = 1;
                break;
            case bel_BEL:
                language_code = 2;
                break;
            case lit_LIT:
                language_code = 3;
                break;
        }
    }

    public String getLoginLabel() {
        return loginLabel[language_code];
    }

    public String getPassLabel() {
        return passLabel[language_code];
    }

    public String getRepeatPassLabel() {
        return repeatPassLabel[language_code];
    }

    public String getLoginButton() {
        return loginButton[language_code];
    }

    public String getRegisterButton() {
        return registerButton[language_code];
    }

    public String getAddButton() {
        return addButton[language_code];
    }

    public String getRemoveButton() {
        return removeButton[language_code];
    }

    public String getRemoveGreaterButton() {
        return removeGreaterButton[language_code];
    }

    public String getRemoveLowerButton() {
        return removeLowerButton[language_code];
    }

    public String getUpdateButton() {
        return updateButton[language_code];
    }

    public String getClearButton() {
        return clearButton[language_code];
    }

    public String getVisButton() {
        return visButton[language_code];
    }

    public String getRefreshButton() {
        return refreshButton[language_code];
    }

    public String getNameLabel() {
        return nameLabel[language_code];
    }

    public String getSalaryLabel() {
        return salaryLabel[language_code];
    }

    public String getPositionLabel() {
        return positionLabel[language_code];
    }

    public String getPersonLabel() {
        return personLabel[language_code];
    }

    public String getCoordLabel() {
        return coordLabel[language_code];
    }

    public String getStartDateLabel() {
        return startDateLabel[language_code];
    }

    public String getEndDateLabel() {
        return endDateLabel[language_code];
    }

    public String getAddIfMaxLabel() {
        return addIfMaxLabel[language_code];
    }

    public String getSubmitButton() {
        return submitButton[language_code];
    }

    public String getBackButton() {
        return backButton[language_code];
    }
}
