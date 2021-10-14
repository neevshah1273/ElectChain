import 'package:flutter/material.dart';

class Home extends StatefulWidget{
  static const id = "Home";

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home>{
  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Text('Working',style: TextStyle(
        color: Colors.blue
      ),),
    );
  }

}