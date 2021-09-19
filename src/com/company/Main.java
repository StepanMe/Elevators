package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final int MIN_FLOOR = 1;
    public static final int MAX_FLOOR = 9;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int moveFrom;
        int moveTo;

        Elevator elevator1 = new Elevator(0, "№1", 1);
        Elevator elevator2 = new Elevator(1, "№2", 1);
        Elevator elevator3 = new Elevator(2, "№3", 1);
        Elevator e1;

        List<Elevator> elevatorList = new ArrayList<>(3);
        elevatorList.add(elevator1);
        elevatorList.add(elevator2);
        elevatorList.add(elevator3);

        int nearestId = -1;

        String inputString;
        do {
            System.out.println("\n************** Новая поездка в лифте **************");
            System.out.print("На какой этаж вызвать лифт? ");
            String fromString = scanner.nextLine();
            while (!isValidFloor(fromString)) {
                System.out.printf("Нажмите кнопку с цифрой от %d до %d: ", MIN_FLOOR, MAX_FLOOR);
                fromString = scanner.nextLine();
            }
            moveFrom = Integer.parseInt(fromString);

            System.out.print("На какой этаж поедем? ");
            String toString = scanner.nextLine();
            while (!isValidFloor(toString)) {
                System.out.printf("Нажмите кнопку с цифрой от %d до %d: ", MIN_FLOOR, MAX_FLOOR);
                toString = scanner.nextLine();
            }
            moveTo = Integer.parseInt(toString);

            for (int i = 0;i <= elevatorList.size() - 1;i++) {
                System.out.println();
                System.out.printf("Лифт %s сейчас на %d-м этаже, до вас ему ехать %d эт.",
                        elevatorList.get(i).getName(),
                        elevatorList.get(i).getCurrentFloor(),
                        elevatorList.get(i).distanceToTheFloor(moveFrom));
            }

            List<Elevator> suitableElevators = new ArrayList<>(elevatorList);
            int c = (int) suitableElevators.stream().filter(it -> it.getCurrentFloor() == 1).count();
            switch (c) {
                case 3:
                    // Если на первом этаже три лифта, берём первый из коллекции
                    e1 = suitableElevators.stream().findFirst().get();
                    nearestId = e1.getId();
                    break;
                case 2:
                    // Если на первом этаже два лифта, сортируем коллекцию по currentFloor (1..9) и пропускаем первый элемент
                    suitableElevators = suitableElevators.stream().sorted((s1, s2) -> s1.compareTo(s2)).skip(1).collect(Collectors.toList());
                    for (int i = 0; i < suitableElevators.size() - 1; i++) {
                        if (suitableElevators.get(i).distanceToTheFloor(moveFrom) <= suitableElevators.get(i+1).distanceToTheFloor(moveFrom)) {
                            nearestId = suitableElevators.get(i).getId();
                        } else {
                            nearestId = suitableElevators.get(i+1).getId();
                        }
                    }
                    break;
                case 1:
                    // Если на первом этаже один лифт, удаляем его и выбираем из оставшихся двух (кто из них ближе)
                    suitableElevators = suitableElevators.stream().filter(it -> it.getCurrentFloor() != 1).collect(Collectors.toList());
                    for (int i = 0; i < suitableElevators.size() - 1; i++) {
                        if (suitableElevators.get(i).distanceToTheFloor(moveFrom) <= suitableElevators.get(i+1).distanceToTheFloor(moveFrom)) {
                            nearestId = suitableElevators.get(i).getId();
                        } else {
                            nearestId = suitableElevators.get(i+1).getId();
                        }
                    }
                    break;
            }
            elevatorList.get(nearestId).setCurrentFloor(moveTo);
            System.out.println("\nК вам приедет лифт " + elevatorList.get(nearestId).getName());

            System.out.print("Едем дальше? (да/нет) ");
            inputString = scanner.nextLine();
        } while (!inputString.equalsIgnoreCase("нет"));
    }

    private static boolean isValidFloor(String input) {
        try {
            int floor = Integer.parseInt(input);
            return floor >= MIN_FLOOR & floor <= MAX_FLOOR;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static class Elevator implements Comparable<Elevator> {
        private int id;
        private String name;
        private Integer currentFloor;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCurrentFloor() {
            return currentFloor;
        }

        public void setCurrentFloor(int currentFloor) {
            this.currentFloor = currentFloor;
        }

        public Elevator(int id, String name, int currentFloor) {
            this.id = id;
            this.name = name;
            this.currentFloor = currentFloor;
        }

        public boolean isOnTheFirstFloor() {
            return this.currentFloor == 1;
        }

        public int distanceToTheFloor(int newFloor) {
            return Math.abs(newFloor - this.currentFloor);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Elevator elevator = (Elevator) o;

            if (id != elevator.id) return false;
            if (currentFloor != elevator.currentFloor) return false;
            return name.equals(elevator.name);
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + name.hashCode();
            result = 31 * result + currentFloor;
            return result;
        }

        @Override
        public int compareTo(Elevator e) {
            return (this.getCurrentFloor() - e.getCurrentFloor());
        }
    }
}